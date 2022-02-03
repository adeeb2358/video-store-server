package woven.video.storage.server.api.utils.file;

import java.io.IOException;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/** @author adeeb2358 */
@Component
@RequiredArgsConstructor
public class Saver {
  @Value("${woven.video.storage.server.api.storage.directory}")
  private static String FILE_DIRECTORY;

  public static String save(MultipartFile file, String fileId) throws IOException {
    String path = FILE_DIRECTORY + "/" + fileId;
    file.transferTo(Paths.get(path));
    return path;
  }
}
