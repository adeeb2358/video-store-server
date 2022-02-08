package woven.video.storage.server.api.utils.file;

import java.io.File;
import java.io.IOException;
import lombok.AllArgsConstructor;

/**
 * @author adeeb2358
 */
@AllArgsConstructor
public class Remover {

    public static void remove(String filePath) throws IOException {
        try {
            var fileToDelete = new File(filePath);
            fileToDelete.delete();
        } catch (Exception exception) {

        }
    }
}
