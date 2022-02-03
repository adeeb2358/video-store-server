package woven.video.storage.server.api.services.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.repos.VideoFileRepository;
import woven.video.storage.server.api.services.VideoFileService;
import woven.video.storage.server.api.utils.file.FormatChecker;

@Service
@RequiredArgsConstructor
/** @author adeeb2358 */
public class VideoFileServiceImpl implements VideoFileService {

  private final VideoFileRepository repository;

  @Override
  public void create(MultipartFile file) throws InvalidFileFormatException, IOException {

    var checkSum = validateFile(file);
    var videoFile =
        VideoFile.builder()
            .checkSum(checkSum)
            .format(file.getContentType())
            .createdAt(LocalDate.now().toString())
            .name(file.getName())
            .size(String.valueOf(file.getSize()))
            .build();
    videoFile.save(file);
    repository.save(videoFile);
  }

  @Override
  public void delete(UUID fileId) throws IOException {
    var videoFile = repository.findById(fileId.toString()).orElseThrow(FileNotFoundException::new);
    videoFile.delete();
    repository.delete(videoFile);
  }

  @Override
  public List<VideoFile> list() {
    return repository.findAll();
  }

  @Override
  public VideoFile get(UUID fileId) throws FileNotFoundException {
    return repository.findById(fileId.toString()).orElseThrow(FileNotFoundException::new);
  }

  private String validateFile(MultipartFile file) throws InvalidFileFormatException, IOException {
    if (!FormatChecker.isFormatAccepted(file)) {
      throw new InvalidFileFormatException();
    }
    var checkSum = DigestUtils.md5DigestAsHex(file.getInputStream());
    var sameFileList = repository.findByCheckSum(checkSum);
    if (sameFileList.isPresent()) {
      throw new FileAlreadyExistsException("");
    }
    return checkSum;
  }

  @AllArgsConstructor
  @Getter
  @Setter
  public class InvalidFileFormatException extends Exception {}
}
