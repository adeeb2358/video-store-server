package woven.video.storage.server.api.utils.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** @author adeeb2358 */
@Component
@RequiredArgsConstructor
public class Remover {
  @Value("${woven.video.storage.server.api.storage.directory}")
  private static String FILE_DIRECTORY;

  public static void remove(String filePath) throws IOException {
    var fileToDelete = new File(FILE_DIRECTORY + filePath);
    if (!fileToDelete.delete()) {
      throw new FileNotFoundException();
    }
  }
}
