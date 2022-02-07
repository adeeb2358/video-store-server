package woven.video.storage.server.api.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import woven.video.storage.server.api.services.VideoFileService;
import woven.video.storage.server.api.testutils.WithTestSetup;
import ws.schild.jave.EncoderException;

class WebmConverterIT extends WithTestSetup {
  @Autowired private WebmConverter webmConverter;
  @Autowired private VideoFileService videoFileService;

  @BeforeEach
  void setup() {}

  @Test
  @DisplayName("Succeeds when proper file is passed for conversion")
  void succeedsWhenProperInput() throws EncoderException {}
}
