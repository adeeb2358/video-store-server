package woven.video.storage.server.api.services.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.services.VideoFileService;
import woven.video.storage.server.api.services.impl.VideoFileServiceImpl.InvalidFileFormatException;
import woven.video.storage.server.api.testutils.WithTestContainers;

@DisplayName("Video file  Service Test")
class VideoFileServiceImplTest extends WithTestContainers {
  @Autowired private VideoFileService service;

  private static final String FILE_NAME = "test-video.mp4";
  private static final String NAME = "test-video";
  private static final String VIDEO_MP_4 = "video/mp4";
  private static final String VIDEO_MPEG = "video/mpeg";
  private static final String DIR = WithTestContainers.DIR;

  private MockMultipartFile createMultipartFile(String fileFormat,String content) {
    return new MockMultipartFile(NAME, FILE_NAME, fileFormat, content.getBytes());
  }

  private void validateFile(VideoFile videoFile, MultipartFile multipartFile) throws IOException {

    Assertions.assertEquals(
        videoFile.getCheckSum(), DigestUtils.md5DigestAsHex(multipartFile.getBytes()));
    Assertions.assertEquals(videoFile.getFilePath(), DIR + videoFile.getId());
    Assertions.assertTrue(!videoFile.getCreatedAt().isBlank());
    Assertions.assertTrue(videoFile.getBinaryContent().isFile());
    Assertions.assertEquals(videoFile.getName(), multipartFile.getOriginalFilename());
    Assertions.assertEquals(videoFile.getFormat(), multipartFile.getContentType());
  }

  private void validateFileContents(VideoFile videoFile, VideoFile searchResult) {
    Assertions.assertEquals(videoFile.getId(), searchResult.getId());
    Assertions.assertEquals(videoFile.getCheckSum(), searchResult.getCheckSum());
    Assertions.assertEquals(videoFile.getFilePath(), searchResult.getFilePath());
    Assertions.assertEquals(videoFile.getCreatedAt(), searchResult.getCreatedAt());
    Assertions.assertEquals(videoFile.getBinaryContent(), searchResult.getBinaryContent());
    Assertions.assertEquals(videoFile.getName(), searchResult.getName());
    Assertions.assertEquals(videoFile.getFormat(), searchResult.getFormat());
  }

  @Nested
  @DisplayName("Store Video file")
  class store {

    @ParameterizedTest
    @ValueSource(strings = {VIDEO_MPEG, VIDEO_MP_4})
    @DisplayName("Succeeds on valid file saving")
    void succeedsOnValidFile(String fileFormat) throws InvalidFileFormatException, IOException {
      var multipartFile = createMultipartFile(fileFormat,"video");
      var result = service.create(multipartFile);
      validateFile(result, multipartFile);
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
          "video/3gpp",
          "video/quicktime",
          "video/x-msvideo",
          "txt",
          "",
          "audio/mp3",
          "image/jpeg"
        })
    @DisplayName("Fails on invalid file formats")
    void failsOnInValidFileFormats(String fileFormat)
        throws InvalidFileFormatException, IOException {
      var multipartFile = createMultipartFile(fileFormat,"video");
      Assertions.assertThrows(
          InvalidFileFormatException.class, () -> service.create(multipartFile));
    }

    @Test
    @DisplayName("Fails on duplicate files")
    void failsOnDuplicateFiles() throws InvalidFileFormatException, IOException {
      var multipartFile = createMultipartFile(VIDEO_MPEG,"video");
      var result = service.create(multipartFile);
      Assertions.assertThrows(
          FileAlreadyExistsException.class, () -> service.create(multipartFile));
    }
  }

  @Nested
  @DisplayName("Get One Video File")
  class get {
    @Test
    @DisplayName("Succeeds when file id is valid")
    void whenFileIsValid() throws InvalidFileFormatException, IOException {
      var multipartFile = createMultipartFile(VIDEO_MPEG,"video");
      var result = service.create(multipartFile);
      var getResult = service.get(result.getId());
      validateFileContents(result, getResult);
    }

    @Test
    @DisplayName("Fails when fileid is invalid")
    void failsWhenInvalidFileId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> service.get(null));
      Assertions.assertThrows(
          FileNotFoundException.class, () -> service.get(UUID.randomUUID().toString()));
    }
  }

  @Nested
  @DisplayName("List Video Files")
  class list {
    @Test
    @DisplayName("Succeeds on listing items")
    void succeedsOnListing() throws InvalidFileFormatException, IOException {
      var multipartFileOne = createMultipartFile(VIDEO_MPEG,"videOne");
      var resultOne = service.create(multipartFileOne);
      var multipartFileTwo = createMultipartFile(VIDEO_MP_4,"videoTwo");
      var resultTwo = service.create(multipartFileTwo);

      var videoFileList = service.list();
      Assertions.assertEquals(videoFileList.size(), 2);
      validateFileContents(resultOne, videoFileList.get(0));
      validateFileContents(resultTwo, videoFileList.get(1));
    }
  }

  @Nested
  @DisplayName("Delete Video File")
  class delete {
    @Test
    @DisplayName("Succeeds on valid file")
    void deleteSucceeds() throws InvalidFileFormatException, IOException {
      var multipartFile = createMultipartFile(VIDEO_MPEG,"video");
      var result = service.create(multipartFile);
      Assertions.assertEquals(result.getId(), service.delete(result.getId()));
    }

    @Test
    @DisplayName("Fails on invalid fileId")
    void deleteFails() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(null));
      Assertions.assertThrows(FileNotFoundException.class, () -> service.delete("INVALID"));
      Assertions.assertThrows(
          FileNotFoundException.class, () -> service.delete(UUID.randomUUID().toString()));
    }
  }
}
