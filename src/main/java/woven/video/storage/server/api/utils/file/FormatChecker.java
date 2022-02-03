package woven.video.storage.server.api.utils.file;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/** @author adeeb2358 */
public class FormatChecker {
  private static final List<String> ACCEPTED_FILE_TYPES = List.of("video/mp4", "video/mpeg");

  public static boolean isFormatAccepted(MultipartFile multipartFile) {
    return ACCEPTED_FILE_TYPES.contains(multipartFile.getContentType());
  }
}
