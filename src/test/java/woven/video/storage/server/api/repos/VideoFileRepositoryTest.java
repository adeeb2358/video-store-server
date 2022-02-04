package woven.video.storage.server.api.repos;

import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.testutils.WithTestContainers;

@DisplayName("Video File Repository Test")
class VideoFileRepositoryTest extends WithTestContainers {
  @Autowired private VideoFileRepository repository;
  private MultipartFile multipartFile;
  private VideoFile videoFile;
  private static final String FILE_NAME = "test-video.mp4";
  private static final String NAME = "test-video";
  private static final String CONTENT_TYPE = "video/mp4";
  private static final String DIR = WithTestContainers.DIR;

  @BeforeEach
  void setRepository() throws IOException {
    multipartFile = new MockMultipartFile(NAME, FILE_NAME, CONTENT_TYPE, "video".getBytes());
    var checkSum = DigestUtils.md5DigestAsHex(multipartFile.getBytes());
    videoFile =
        VideoFile.builder()
            .checkSum(checkSum)
            .format(multipartFile.getContentType())
            .createdAt(LocalDate.now().toString())
            .name(multipartFile.getOriginalFilename())
            .size(String.valueOf(multipartFile.getSize()))
            .build();
  }

  @DisplayName("Insert Documents to Video File Repository")
  @Nested
  class insert {
    @Test
    @DisplayName("Succeeds on saving one video information")
    void saveInformation() throws IOException {
      var result = repository.save(videoFile);
      result.setFilePath(DIR + result.getFilePath());
      result.save(multipartFile);
      result = repository.save(result);
      Assertions.assertEquals(result.getCheckSum(), videoFile.getCheckSum());
    }
  }

  @Nested
  @DisplayName("Remove a document from Video File Repository")
  class remove {}

  @Nested
  @DisplayName("Get documents from Video File Repository")
  class get {}
}
