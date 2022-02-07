package woven.video.storage.server.api.converters;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import woven.video.storage.server.api.services.VideoFileService;
import woven.video.storage.server.api.services.impl.VideoFileServiceImpl.InvalidFileFormatException;
import woven.video.storage.server.api.testutils.WithTestSetup;
import ws.schild.jave.EncoderException;

@DisplayName(
    "mp4 and mpeg to webm conversion test "
        + "depending on your docker memory and cpu [usually 5 - 10 mins on each test case]]")
class WebmConverterIT extends WithTestSetup {
  @Autowired private WebmConverter webmConverter;
  @Autowired private VideoFileService videoFileService;
  private static final String MP4_FILE_PATH = "test-video-files/test-file.mp4";
  private static final String MPEG_FILE_PATH = "test-video-files/test-file.mpeg";

  @ParameterizedTest
  @ValueSource(strings = {MP4_FILE_PATH, MPEG_FILE_PATH})
  @DisplayName("Succeeds when proper file is passed for conversion[This test will take more time ")
  void succeedsWhenProperInput(String filePath)
      throws EncoderException, IOException, InvalidFileFormatException {
    // var videoFileOne = videoFileService.create(createMultiPartFile(filePath));
    // webmConverter.convert(videoFileOne);
  }
}
