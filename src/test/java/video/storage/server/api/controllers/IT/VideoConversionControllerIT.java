package video.storage.server.api.controllers.IT;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import video.storage.server.api.controllers.VideoConversionController;
import video.storage.server.api.converters.WebmConverter;
import video.storage.server.api.documents.VideoFile.Status;
import video.storage.server.api.repos.VideoFileRepository;
import video.storage.server.api.services.VideoFileService;
import video.storage.server.api.testutils.WithTestSetup;

@DisplayName("Sequential operation of convert, download, lit and reconvert")
@AutoConfigureMockMvc
class VideoConversionControllerIT extends WithTestSetup {

    @Autowired
    private VideoConversionController controller;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VideoFileRepository repository;

    @Autowired
    private VideoFileService videoFileController;

    @Autowired
    private WebmConverter converter;

    private static final String API_END_POINT = "/v1/file-conversion";
    private static final String MP4_FILE_PATH = "test-video-files/test-file.mp4";
    private static final String MPEG_FILE_PATH = "test-video-files/test-file.mpeg";

    @ParameterizedTest
    @ValueSource(strings = {MP4_FILE_PATH, MPEG_FILE_PATH})
    void scenario(String filePath) throws Exception {
        var video = videoFileController.create(createMultiPartFile(filePath));
        // perform conversion operation
        var result = performConversion(video.getId())
                .andExpect(status().isCreated())
                .andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("File Added to "
                + "Queue fileId:" + video.getId()));

        // download or get result
        result = performDownload(video.getId())
                .andExpect(status().is(not(HttpStatus.NO_CONTENT)))
                .andExpect(status().is(not(HttpStatus.BAD_REQUEST)))
                .andExpect(status().is(not(HttpStatus.CONFLICT)))
                .andExpect(status().is(not(HttpStatus.NOT_FOUND)))
                .andReturn();

        // list items in progress
        result = performList()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fileid", is(video.getId())))
                .andExpect(jsonPath("$[0].name", is(video.getConvertedName())))
                .andExpect(jsonPath("$[0].status", is(Matchers.not(Status.NOT_CONVERTED))))
                .andReturn();

        video.conversionFailed("Unknown reason");
        // reconvert failed item
        result = performReconvert(video.getId())
                .andExpect(status().is(not(HttpStatus.NO_CONTENT)))
                .andExpect(status().is(not(HttpStatus.BAD_REQUEST)))
                .andExpect(status().is(not(HttpStatus.CONFLICT)))
                .andExpect(status().is(not(HttpStatus.NOT_FOUND)))
                .andReturn();
    }

    @Nested
    @DisplayName("Download Error case")
    class get {

        @Test
        @DisplayName("Fails when invalid file id is given")
        void whenInvalidFileIdIsGiven() throws Exception {
            performDownload("INVALID_ID").andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Reconvert Error case")
    class reconvert {

        @Test
        @DisplayName("Fails when invalid file id is given")
        void whenInvalidFileIdIsGiven() throws Exception {
            performReconvert("INVALID_ID").andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("convert Error case")
    class convert {

        @Test
        @DisplayName("Fails when invalid file id is given")
        void whenInvalidFileIdIsGiven() throws Exception {
            performConversion("INVALID_ID").andExpect(status().isNotFound());
        }
    }


    //download the binary file
    @Test
    @DisplayName("Succeeds Downloading converted file")
    void downloadFile() throws Exception {
        var video = videoFileController.create(createMultiPartFile(MPEG_FILE_PATH));
        video.conversionQueued();
        video = converter.convert(video);
        var result = performDownload(video.getId()).andExpect(status().isOk()).andReturn();
        Assertions.assertNotNull(result.getResponse());
    }

    private ResultActions performConversion(String fileId) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(API_END_POINT + "/" + fileId));
    }

    private ResultActions performDownload(String fileId) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(API_END_POINT + "/" + fileId));
    }

    private ResultActions performList() throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.get(API_END_POINT).contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performReconvert(String fileId) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(API_END_POINT + "/" + fileId));
    }

}