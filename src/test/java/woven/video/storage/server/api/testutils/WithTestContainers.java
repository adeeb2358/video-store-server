package woven.video.storage.server.api.testutils;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import woven.video.storage.server.api.repos.VideoFileRepository;
import woven.video.storage.server.api.services.impl.VideoFileServiceImplConfig;

@Import(VideoFileServiceImplConfig.class)
@SpringBootTest(properties = {"woven.video.storage.server.api.storage.directory=test-video-dir"})
public class WithTestContainers {
  @Container static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
  public static final String DIR = "test-video-dir/";

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
  }

  static {
    mongoDBContainer.start();
  }

  @Autowired private VideoFileRepository videoFileRepository;

  @BeforeEach
  void setupTestDir() throws IOException {
    var testDir = new File(DIR);
    if (!testDir.exists()) {
      testDir.mkdir();
    } else {
      FileUtils.cleanDirectory(testDir);
    }
  }

  @AfterEach
  void cleanUp() throws IOException {
    this.videoFileRepository.deleteAll();
    cleanTestDir();
  }

    public static void cleanTestDir() throws IOException {
        var testDir = new File(DIR);
        FileUtils.cleanDirectory(testDir);
        FileUtils.deleteDirectory(testDir);
    }
}
