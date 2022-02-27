package video.storage.server.api.controllers.dtos.views.files;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import video.storage.server.api.documents.VideoFile;

/**
 * The type File view.
 *
 * @author adeeb2358
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class FileView {

    /**
     * Delete response response entity.
     *
     * @param fileId the file id
     * @return the response entity
     */
    public static ResponseEntity<String> deleteResponse(String fileId) {
    return new ResponseEntity<String>(
        "File Removed Successfully. fileId:" + fileId, HttpStatus.NO_CONTENT);
  }

    /**
     * Create response response entity.
     *
     * @param videoFile the video file
     * @return the response entity
     */
    public static ResponseEntity<String> createResponse(VideoFile videoFile) {
    return new ResponseEntity<String>(
        "File Uploaded Successfully. fileId:" + videoFile.getId(), HttpStatus.CREATED);
  }
}
