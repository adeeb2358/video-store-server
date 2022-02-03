package woven.video.storage.server.api.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.services.impl.VideoFileServiceImpl.InvalidFileFormatException;

/** @author adeeb2358 */
public interface VideoFileService {
  void create(MultipartFile file) throws InvalidFileFormatException, IOException;

  void delete(UUID fileId) throws IOException;

  List<VideoFile> list();

  VideoFile get(UUID fileId) throws FileNotFoundException;
}
