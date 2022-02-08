package woven.video.storage.server.api.services.impl;

import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import woven.video.storage.server.api.converters.WebmConverter;
import woven.video.storage.server.api.documents.VideoFile.Status;
import woven.video.storage.server.api.repos.VideoFileRepository;
import woven.video.storage.server.api.services.VideoFileService;
import woven.video.storage.server.api.services.impl.VideoFileServiceImpl.InvalidFileFormatException;
import woven.video.storage.server.api.testutils.WithTestSetup;

@DisplayName("Video file conversion service")
class VideoConversionServiceImplTest extends WithTestSetup {

    @Autowired
    private VideoFileRepository repository;

    @Autowired
    private WebmConverter converter;

    @Autowired
    private VideoFileService service;

    private VideoConversionServiceImpl conversionService;

    private static final String MP4_FILE_PATH = "test-video-files/test-file.mp4";
    private static final String MPEG_FILE_PATH = "test-video-files/test-file.mpeg";

    @BeforeEach
    void setup() {
        conversionService = new VideoConversionServiceImpl(repository, converter, 10);
    }

    @AfterEach
    void clearData() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Create succeeds on valid input ")
    void create() throws IOException, InvalidFileFormatException, InterruptedException {
        var videoFileOne = service.create(WithTestSetup.createMultiPartFile(MPEG_FILE_PATH));
        var result = conversionService.create(videoFileOne.getId());
        Assertions.assertEquals(result.getStatus(), Status.QUEUED);
    }

    @Test
    @DisplayName("Succeeds Listing items in conversion")
    void list() throws IOException, InvalidFileFormatException {
        var videoFileOne = service.create(WithTestSetup.createMultiPartFile(MPEG_FILE_PATH));
        var videoFileTwo = service.create(WithTestSetup.createMultiPartFile(MP4_FILE_PATH));
        videoFileOne.conversionQueued();
        videoFileTwo.conversionFinished();
        repository.save(videoFileOne);
        repository.save(videoFileTwo);
        var list = conversionService.list();
        Assertions.assertEquals(list.get(0).getStatus(), Status.QUEUED);
        Assertions.assertNotNull(list.get(1).getStatus());
    }

    @Test
    @DisplayName("Succeeds in conversion Of failed items")
    void convertAgain() throws IOException, InvalidFileFormatException, InterruptedException {
        var videoFileOne = service.create(WithTestSetup.createMultiPartFile(MPEG_FILE_PATH));
        videoFileOne.conversionFailed("Unknown Reason");
        repository.save(videoFileOne);
        var videoFile = conversionService.convertFailed(videoFileOne.getId());
        Assertions.assertEquals(videoFile.getStatus(), Status.QUEUED);
    }
}