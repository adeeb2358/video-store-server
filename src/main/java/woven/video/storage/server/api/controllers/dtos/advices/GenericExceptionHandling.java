package woven.video.storage.server.api.controllers.dtos.advices;

import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import woven.video.storage.server.api.controllers.dtos.views.conversions.FileConversionGetView.OperationNotCompletedException;
import woven.video.storage.server.api.services.impl.VideoFileServiceImpl.InvalidFileFormatException;

/**
 * The type Generic exception handling.
 *
 * @author adeeb2358
 */
@ControllerAdvice
public class GenericExceptionHandling {

    /**
     * Handle response entity.
     *
     * @param handled the handled
     * @return the response entity
     */
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<String> handle(FileNotFoundException handled) {
        return new ResponseEntity<String>("File not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Handle response entity.
     *
     * @param handled the handled
     * @return the response entity
     */
    @ExceptionHandler(InvalidFileFormatException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    ResponseEntity<String> handle(InvalidFileFormatException handled) {
        return new ResponseEntity<String>("Invalid video file uploaded",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Handle response entity.
     *
     * @param handled the handled
     * @return the response entity
     */
    @ExceptionHandler(FileAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ResponseEntity<String> handle(FileAlreadyExistsException handled) {
        return new ResponseEntity<String>("File Already Exits fileId:" + handled.getMessage(),
                HttpStatus.CONFLICT);
    }

    /**
     * Handle response entity.
     *
     * @param handled the handled
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handle(Exception handled) {
        return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle response entity.
     *
     * @param handled the handled
     * @return the response entity
     */
    @ExceptionHandler(OperationNotCompletedException.class)
    ResponseEntity<String> handle(OperationNotCompletedException handled) {
        return new ResponseEntity<String>(handled.getMessage(), handled.getHttpStatus());
    }
}
