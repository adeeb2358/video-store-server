package woven.video.storage.server.api.controllers.dtos.views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** @author adeeb2358 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class VideoFileView {
  public static ResponseEntity<String> getDeleteResponse() {
    return new ResponseEntity<String>("File was successfully removed", HttpStatus.NO_CONTENT);
  }

  public static ResponseEntity<String> getCreateResponse() {
    return new ResponseEntity<String>("File Uploaded", HttpStatus.CREATED);
  }
}
