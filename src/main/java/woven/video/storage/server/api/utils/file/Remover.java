package woven.video.storage.server.api.utils.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.AllArgsConstructor;

/** @author adeeb2358 */
@AllArgsConstructor
public class Remover {
    public static void remove(String filePath) throws IOException {
    var fileToDelete = new File(filePath);
    if (!fileToDelete.delete()) {
      throw new FileNotFoundException();
    }
  }
}
