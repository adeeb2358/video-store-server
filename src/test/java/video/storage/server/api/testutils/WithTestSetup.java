package video.storage.server.api.testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import video.storage.server.api.converters.configurations.ConverterConfiguration;
import video.storage.server.api.services.config.VideoFileServiceConfig;
import video.storage.server.api.repos.VideoFileRepository;

@Import({VideoFileServiceConfig.class, ConverterConfiguration.class})
@SpringBootTest(properties = {"woven.video.storage.server.api.storage.directory=test-video-dir"})
public class WithTestSetup {

  public static File readVideoFile(String filePath) throws FileNotFoundException {
    return new File(WithTestSetup.class.getClassLoader().getResource(filePath).getFile());
  }

  public static MockMultipartFile createMultiPartFile(String filePath) throws IOException {
    var file = readVideoFile(filePath);
    return new MockMultipartFile(
        "file",
        Path.of(filePath).getFileName().toString(),
        Files.probeContentType(Path.of(filePath)),
        IOUtils.toByteArray(new FileInputStream(file)));
  }

  @Container static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
  public static final String DIR = "test-video-dir/";

  public static final String CON_DIR = DIR + "converted/";

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
  }

  public static String getDateTimeFormatted(LocalDateTime datetime) throws ParseException {
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return datetime.format(formatter);
  }

  static {
    mongoDBContainer.start();
  }

  @Autowired private VideoFileRepository videoFileRepository;

  void cleanOrCreateDir(String DIR) throws IOException {
    var testDir = new File(DIR);
    if (!testDir.exists()) {
      testDir.mkdir();
    } else {
      FileUtils.cleanDirectory(testDir);
    }
  }

  @BeforeEach
  void setupTestDir() throws IOException {
    cleanOrCreateDir(DIR);
    cleanOrCreateDir(CON_DIR);
  }

  @AfterEach
  void cleanUp() throws IOException {
    this.videoFileRepository.deleteAll();
      cleanTestDir(CON_DIR);
      cleanTestDir(DIR);
  }

  public static void cleanTestDir(String DIR) throws IOException {
    var testDir = new File(DIR);
    FileUtils.cleanDirectory(testDir);
    FileUtils.deleteDirectory(testDir);
  }
}
