package woven.video.storage.server.api.controllers.dtos.advices;

import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import woven.video.storage.server.api.services.impl.VideoFileServiceImpl.InvalidFileFormatException;

/** @author adeeb2358 */
@ControllerAdvice
public class GenericExceptionHandling {

  @ExceptionHandler(FileNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ResponseEntity<String> handle(FileNotFoundException handled) {
    return new ResponseEntity<String>("File not found", HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidFileFormatException.class)
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  ResponseEntity<String> handle(InvalidFileFormatException handled) {
    return new ResponseEntity<String>("Bad request", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @ExceptionHandler(FileAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  ResponseEntity<String> handle(FileAlreadyExistsException handled) {
    return new ResponseEntity<String>("Bad request", HttpStatus.CONFLICT);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ResponseEntity<String> handle(Exception handled) {
    return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
  }
}
