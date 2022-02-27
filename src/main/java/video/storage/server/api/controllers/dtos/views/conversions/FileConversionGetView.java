package video.storage.server.api.controllers.dtos.views.conversions;

import javax.naming.OperationNotSupportedException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import video.storage.server.api.documents.VideoFile;
import video.storage.server.api.documents.VideoFile.Status;

/**
 * The type File conversion get view.
 *
 * @author adeeb2358
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileConversionGetView {

    /**
     * From response entity.
     *
     * @param file the file
     * @return the response entity
     * @throws OperationNotSupportedException the operation not supported exception
     * @throws OperationNotCompletedException the operation not completed exception
     */
    public static ResponseEntity<Resource> from(VideoFile file)
            throws OperationNotSupportedException, OperationNotCompletedException {
        if (file.getStatus().equals(Status.FINISHED)) {
            return getDownloadContent(file);
        }

        throw generatedException(file);
    }

    private static ResponseEntity<Resource> getDownloadContent(VideoFile file) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getConvertedName() +
                                "\"")
                .body(new FileSystemResource(file.getConvertedContent()));
    }

    private static OperationNotCompletedException generatedException(VideoFile file) {
        String message = "";
        HttpStatus status = HttpStatus.ACCEPTED;
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
        }
        return OperationNotCompletedException.from(message, status);
    }

    /**
     * The type Operation not completed exception.
     */
    @AllArgsConstructor
    @Getter
    public static class OperationNotCompletedException extends Exception {

        private final String message;
        private final HttpStatus httpStatus;

        /**
         * From operation not completed exception.
         *
         * @param message    the message
         * @param httpStatus the http status
         * @return the operation not completed exception
         */
        public static OperationNotCompletedException from(String message, HttpStatus httpStatus) {
            return new OperationNotCompletedException(message, httpStatus);
        }
    }

    ;


}
