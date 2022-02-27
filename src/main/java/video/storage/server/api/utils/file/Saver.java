package video.storage.server.api.utils.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Saver.
 *
 * @author adeeb2358
 */
@AllArgsConstructor
public class Saver {
  private static String FILE_DIRECTORY = "video-store";

    /**
     * Save string.
     *
     * @param file     the file
     * @param filePath the file path
     * @return the string
     * @throws IOException the io exception
     */
    public static String save(MultipartFile file, String filePath) throws IOException {
    if (!new File(filePath).exists()) {
      file.transferTo(Paths.get(filePath));
      return filePath;
    }
    throw new FileAlreadyExistsException("");
  }
}
