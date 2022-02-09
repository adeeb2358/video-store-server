package woven.video.storage.server.api.utils.file;

import java.io.File;
import java.io.FileNotFoundException;
import lombok.AllArgsConstructor;

/**
 * The type Reader.
 *
 * @author adeeb2358
 */
@AllArgsConstructor
public class Reader {

    /**
     * Read file.
     *
     * @param filePath the file path
     * @return the file
     * @throws FileNotFoundException the file not found exception
     */
    public static File read(String filePath) throws FileNotFoundException {
    var file = new File(filePath);
    if (file.exists()) {
      return new File(filePath);
    }
    throw new FileNotFoundException("");
  }
}
