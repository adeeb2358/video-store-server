package woven.video.storage.server.api.controllers.dtos.views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import woven.video.storage.server.api.documents.VideoFile;

/** @author adeeb2358 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class VideoFileView {
  public static ResponseEntity<String> deleteResponse(String fileId) {
    return new ResponseEntity<String>(
        "File Removed Successfully. fileId:" + fileId, HttpStatus.NO_CONTENT);
  }

  public static ResponseEntity<String> createResponse(VideoFile videoFile) {
    return new ResponseEntity<String>(
        "File Uploaded Successfully. fileId:" + videoFile.getId(), HttpStatus.CREATED);
  }
}
