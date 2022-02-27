package video.storage.server.api.converters;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import video.storage.server.api.documents.VideoFile.Status;
import video.storage.server.api.services.VideoFileService;
import video.storage.server.api.services.impl.VideoFileServiceImpl.InvalidFileFormatException;
import video.storage.server.api.testutils.WithTestSetup;
import ws.schild.jave.EncoderException;

@Tag("video-file-convert")
@DisplayName(
        "mp4 and mpeg to webm conversion test "
                + "depending on your docker memory and cpu [usually 5 - 10 mins on each test "
                + "case]]")
class WebmConverterTest extends WithTestSetup {

    @Autowired
    private WebmConverter webmConverter;
    @Autowired
    private VideoFileService videoFileService;
    private static final String MP4_FILE_PATH = "test-video-files/test-file.mp4";
    private static final String MPEG_FILE_PATH = "test-video-files/test-file.mpeg";

    @ParameterizedTest
    @ValueSource(strings = {MP4_FILE_PATH, MPEG_FILE_PATH})
    @DisplayName("Succeeds when proper file is passed for conversion[This test will take more "
            + "time ")
    void succeedsWhenProperInput(String filePath)
            throws EncoderException, IOException, InvalidFileFormatException {
        var videoFileOne = videoFileService.create(createMultiPartFile(filePath));
        var result = webmConverter.convert(videoFileOne);
        Assertions.assertEquals(result.getStatus(), Status.FINISHED);
        var convertedFile = videoFileOne.getConvertedContent();
        Assertions.assertTrue(convertedFile.isFile());
        Assertions.assertEquals(convertedFile.getName(), result.getId());
        Assertions.assertEquals(result.getProgress(), 100.0);
    }
}
