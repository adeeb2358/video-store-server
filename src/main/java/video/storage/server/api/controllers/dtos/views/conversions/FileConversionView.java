package video.storage.server.api.controllers.dtos.views.conversions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import video.storage.server.api.documents.VideoFile;
import video.storage.server.api.documents.VideoFile.Status;

/**
 * The type File conversion view.
 */
@Getter
@Setter
public class FileConversionView {

    /**
     * From response entity.
     *
     * @param file the file
     * @return the response entity
     */
    public static ResponseEntity<String> from(VideoFile file) {
        String message = "Unknown error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (file.getStatus().equals(Status.QUEUED)) {
            message = "File Added to Queue fileId:" + file.getId();
            status = HttpStatus.CREATED;
        } else if (file.getStatus().equals(Status.IN_PROGRESS)) {
            message = "File Conversion in progress. " + "Percentage : " + file.getProgress() + " "
                    + "fileId:" + file.getId();
            status = HttpStatus.PROCESSING;
        } else if (file.getStatus().equals(Status.FAILED)) {
            message =
                    "Failed Conversion." + " Reason:" + file.getFailureMessage() + " fileId:" + file
                            .getId();
            status = HttpStatus.NO_CONTENT;
        } else if (file.getStatus().equals(Status.FINISHED)) {
            status = HttpStatus.OK;
            message = "File Processing Completed";
        }
        return ResponseEntity.status(status).body(message);
    }
}
