package woven.video.storage.server.api.utils.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/** @author adeeb2358 */
@AllArgsConstructor
@Configuration
public class Reader {

  @Value("${woven.video.storage.server.api.storage.directory}")
  private static String FILE_DIRECTORY;

  public static File read(String filePath) throws IOException {
    return new File(FILE_DIRECTORY + filePath);
  }
}
