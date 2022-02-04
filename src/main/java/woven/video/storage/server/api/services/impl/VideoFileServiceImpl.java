package woven.video.storage.server.api.services.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.repos.VideoFileRepository;
import woven.video.storage.server.api.services.VideoFileService;
import woven.video.storage.server.api.utils.file.FormatChecker;

@AllArgsConstructor
/** @author adeeb2358 */
public class VideoFileServiceImpl implements VideoFileService {

  private final VideoFileRepository repository;
  private final String VIDEO_STORE_DIR;

  @Override
  public VideoFile create(MultipartFile file) throws InvalidFileFormatException, IOException {

    var checkSum = validateAndGetChecksum(file);
    var videoFile =
        repository.save(
            VideoFile.builder()
                .checkSum(checkSum)
                .format(file.getContentType())
                .name(file.getOriginalFilename())
                .size(String.valueOf(file.getSize()))
                .build());
    videoFile.setFilePath(VIDEO_STORE_DIR + "/" + videoFile.getId());
    videoFile.save(file);
    return repository.save(videoFile);
  }

  @Override
  public String delete(String fileId) throws IOException {
    var videoFile = repository.findById(fileId).orElseThrow(FileNotFoundException::new);
    videoFile.delete();
    repository.delete(videoFile);
    return fileId;
  }

  @Override
  public List<VideoFile> list() {
    return repository.findAll();
  }

  @Override
  public VideoFile get(String fileId) throws FileNotFoundException {
    return repository.findById(fileId).orElseThrow(FileNotFoundException::new);
  }

  private String validateAndGetChecksum(MultipartFile file)
      throws InvalidFileFormatException, IOException {
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
