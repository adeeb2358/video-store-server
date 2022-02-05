package woven.video.storage.server.api.utils.file;

import java.io.File;
import java.io.FileNotFoundException;
import lombok.AllArgsConstructor;

/** @author adeeb2358 */
@AllArgsConstructor
public class Reader {

  public static File read(String filePath) throws FileNotFoundException {
    var file = new File(filePath);
    if (file.exists()) {
      return new File(filePath);
    }
    throw new FileNotFoundException("");
  }
}
