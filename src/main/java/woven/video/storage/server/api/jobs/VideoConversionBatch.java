package woven.video.storage.server.api.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Log4j2
/** @author adeeb2358 */
@ConditionalOnProperty(
    value = "${jobs.video-conversion.enabled}",
    havingValue = "true",
    matchIfMissing = true)
@Configuration
@RequiredArgsConstructor
public class VideoConversionBatch {
  @Scheduled(cron = "${jobs.video-conversion.cron}", zone = "${jobs.video-conversion.timezone:JST}")
  public void execute() {
    log.info("job is running");
  }
}
