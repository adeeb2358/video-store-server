package video.storage.server.api.utils.file;

import java.io.File;
import java.io.IOException;
import lombok.AllArgsConstructor;

/**
 * The type Remover.
 *
 * @author adeeb2358
 */
@AllArgsConstructor
public class Remover {

    /**
     * Remove.
     *
     * @param filePath the file path
     * @throws IOException the io exception
     */
    public static void remove(String filePath) throws IOException {
        try {
            var fileToDelete = new File(filePath);
            fileToDelete.delete();
        } catch (Exception exception) {

        }
    }
}
