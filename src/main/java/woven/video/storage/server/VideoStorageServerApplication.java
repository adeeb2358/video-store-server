package woven.video.storage.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/** @author adeeb2358 */
@SpringBootApplication
@EnableAsync
public class VideoStorageServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(VideoStorageServerApplication.class, args);
  }
}
