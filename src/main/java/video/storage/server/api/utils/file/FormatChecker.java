package video.storage.server.api.utils.file;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Format checker.
 *
 * @author adeeb2358
 */
public class FormatChecker {
  private static final List<String> ACCEPTED_FILE_TYPES = List.of("video/mp4", "video/mpeg");

    /**
     * Is format accepted boolean.
     *
     * @param multipartFile the multipart file
     * @return the boolean
     */
    public static boolean isFormatAccepted(MultipartFile multipartFile) {
    return ACCEPTED_FILE_TYPES.contains(multipartFile.getContentType());
  }
}
