package video.storage.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The type Video storage server application.
 *
 * @author adeeb2358
 */
@SpringBootApplication
@EnableAsync
public class VideoStorageServerApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
    SpringApplication.run(VideoStorageServerApplication.class, args);
  }
}
