package woven.video.storage.server.api.converters.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ws.schild.jave.Encoder;

/**
 * The type Converter configuration.
 *
 * @author adeeb2358
 */
@Configuration
public class ConverterConfiguration {

    /**
     * Gets encoder.
     *
     * @return the encoder
     */
    @Bean
  public Encoder getEncoder() {
    return new Encoder();
  }
}
