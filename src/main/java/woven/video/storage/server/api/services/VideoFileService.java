package woven.video.storage.server.api.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.services.impl.VideoFileServiceImpl.InvalidFileFormatException;

/** @author adeeb2358 */
public interface VideoFileService {
  VideoFile create(MultipartFile file) throws InvalidFileFormatException, IOException;

  String delete(String fileId) throws IOException;

  List<VideoFile> list();

  VideoFile get(String fileId) throws FileNotFoundException;
}
