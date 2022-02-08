package woven.video.storage.server.api.converters.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ws.schild.jave.Encoder;

/** @author adeeb2358 */
@Configuration
public class ConverterConfiguration {

  @Bean
  public Encoder getEncoder() {
    return new Encoder();
  }
}
