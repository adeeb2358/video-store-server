package video.storage.server.api.controllers.IT;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import video.storage.server.api.controllers.VideoFileController;
import video.storage.server.api.repos.VideoFileRepository;
import video.storage.server.api.testutils.WithTestSetup;

@DisplayName("Sequential operation of upload, delete,download and list")
@AutoConfigureMockMvc
class VideoFileControllerIT extends WithTestSetup {

    @Autowired
    private VideoFileController controller;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private VideoFileRepository repository;

    private static final String API_END_POINT = "/v1/files";
    private static final String MP4_FILE_PATH = "test-video-files/test-file.mp4";
    private static final String MPEG_FILE_PATH = "test-video-files/test-file.mpeg";


    @ParameterizedTest
    @ValueSource(strings = {MP4_FILE_PATH, MPEG_FILE_PATH})
    @DisplayName("Sequential operation of upload, delete,download and list IT")
    void scenario(String filePath) throws Exception {
        // upload a file
        var multipartFile = WithTestSetup.createMultiPartFile(filePath);
        var uploadResult = performUpload(multipartFile).andExpect(status().isCreated()).andReturn();
        Assertions.assertTrue(
                uploadResult.getResponse().getContentAsString()
                        .contains("File Uploaded Successfully"));
        Assertions.assertEquals(repository.count(), 1);

        // get one video file
        var oneFile = repository.findAll().get(0);
        var getResult = performGet(oneFile.getId()).andExpect(status().isOk()).andReturn();
        Assertions.assertNotNull(getResult.getResponse().getContentAsString());

        // list file
        performList()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fileid", is(oneFile.getId())))
                .andExpect(jsonPath("$[0].name", is(oneFile.getName())))
                .andExpect(jsonPath("$[0].created_at",
                        is(WithTestSetup.getDateTimeFormatted(oneFile.getCreatedAt()))))
                .andExpect(jsonPath("$[0].size", is(oneFile.getSize())))
                .andReturn();

        // delete file from the server
        var deleteResult = performDelete(oneFile.getId()).andExpect(status().isNoContent())
                .andReturn();
        Assertions.assertTrue(
                deleteResult.getResponse().getContentAsString()
                        .contains("File Removed Successfully"));
    }

    @Nested
    @DisplayName("Upload Error Cases")
    class upload {

        @Test
        @DisplayName("Fails when invalid file is uploaded")
        void whenInvalidFileIsUploaded() throws Exception {
            var multipartFile =
                    new MockMultipartFile("file", "INVALID_FILE", "INVALID_CONTENT_TYPE",
                            "video".getBytes());
            performUpload(multipartFile).andExpect(status().isUnsupportedMediaType());
        }

        @Test
        @DisplayName("Fails when duplicate file is uploaded")
        void whenDuplicateFileIsUploaded() throws Exception {
            performUpload(createMultiPartFile(MPEG_FILE_PATH)).andExpect(status().isCreated());
            performUpload(createMultiPartFile(MPEG_FILE_PATH)).andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("Delete Error case")
    class delete {

        @Test
        @DisplayName("Fails when invalid file id is given")
        void whenInvalidFileIdIsGiven() throws Exception {
            performDelete("INVALID_ID").andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Download Error case")
    class get {

        @Test
        @DisplayName("Fails when invalid file id is given")
        void whenInvalidFileIdIsGiven() throws Exception {
            performGet("INVALID_ID").andExpect(status().isNotFound());
        }
    }

    private ResultActions performUpload(MockMultipartFile file) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.multipart(API_END_POINT)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA));
    }

    private ResultActions performGet(String fileId) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(API_END_POINT + "/" + fileId));
    }

    private ResultActions performList() throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.get(API_END_POINT).contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions performDelete(String fileId) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(API_END_POINT + "/" + fileId));
    }
}
