package woven.video.storage.server.api.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.DigestUtils;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

@DisplayName("File utils test")
class FileUtilsTest {

    private static final String FILE_NAME = "test-video.mp4";
    private static final String NAME = "test-video";
    private static final String CONTENT_TYPE = "video/mp4";
    private static final UUID fileId = UUID.randomUUID();
    private static final String DIR = "test-video-dir/";

    @BeforeEach
    void setupTestDir() throws IOException {
        var testDir = new File(DIR);
        if (!testDir.exists()) {
            testDir.mkdir();
        } else {
            FileUtils.cleanDirectory(testDir);
        }
    }

    @AfterAll
    public static void cleanTestDir() throws IOException {
        var testDir = new File(DIR);
        FileUtils.cleanDirectory(testDir);
        FileUtils.deleteDirectory(testDir);
    }

    @Nested
    @DisplayName("Create operations")
    class Create {

        @Test
        @DisplayName("Succeeded when saving file successfully")
        void saveCorrectFile() throws IOException {
            MockMultipartFile multipartFile =
                    new MockMultipartFile(NAME, FILE_NAME, CONTENT_TYPE, "video".getBytes());
            var result = Saver.save(multipartFile, DIR + fileId.toString());
            Assertions.assertEquals(result, DIR + fileId.toString());
        }

        @Test
        @DisplayName("Fails on saving duplicate files")
        void failToSaveDuplicateFile() throws IOException {
            MockMultipartFile multipartFile =
                    new MockMultipartFile(NAME, FILE_NAME, CONTENT_TYPE, "video".getBytes());
            var result = Saver.save(multipartFile, DIR + fileId.toString());
            Assertions.assertThrows(
                    FileAlreadyExistsException.class,
                    () -> Saver.save(multipartFile, DIR + fileId.toString()));
        }

        @ParameterizedTest
        @ValueSource(strings = {"/asas"})
        @DisplayName("Fails on invalid path")
        void failsOnInvalidPath(String path) {
            MockMultipartFile multipartFile =
                    new MockMultipartFile(NAME, FILE_NAME, CONTENT_TYPE, "video".getBytes());
            Assertions.assertThrows(IOException.class, () -> Saver.save(multipartFile, path));
            Assertions.assertThrows(NullPointerException.class, () -> Saver.save(null, null));
        }
    }

    @Nested
    @DisplayName("Delete operation")
    class delete {

        @Test
        @DisplayName("Succeeds deleting valid file")
        void succeedsValidDeletion() throws IOException {
            String filePath = DIR + fileId.toString();
            MockMultipartFile multipartFile =
                    new MockMultipartFile(NAME, FILE_NAME, CONTENT_TYPE, "video".getBytes());
            Saver.save(multipartFile, filePath);
            Assertions.assertDoesNotThrow(() -> Remover.remove(filePath));
        }

        @Test
        @DisplayName("Fails on invalid file path")
        void failsOnInvalidPath() throws IOException {
            Assertions.assertDoesNotThrow(() -> Remover.remove("sdsdd"));
            Assertions.assertDoesNotThrow(() -> Remover.remove(null));
        }
    }

    @Nested
    class read {

        @Test
        @DisplayName("Succeeds on file read")
        void SucceedsOnFileRead() throws IOException {
            String filePath = DIR + fileId.toString();
            MockMultipartFile multipartFile =
                    new MockMultipartFile(NAME, FILE_NAME, CONTENT_TYPE, "video".getBytes());
            Saver.save(multipartFile, filePath);
            var result = Reader.read(filePath);
            Assertions.assertEquals(
                    DigestUtils.md5DigestAsHex(multipartFile.getBytes()),
                    DigestUtils.md5DigestAsHex(new FileInputStream(result)));
        }

        @Test
        @DisplayName("Fails on Invalid path")
        void failsOnInvalidPath() {
            Assertions.assertThrows(FileNotFoundException.class, () -> Reader.read("/sdsdd"));
            Assertions.assertThrows(NullPointerException.class, () -> Reader.read(null));
        }
    }

    @Nested
    @DisplayName("Verification of binary content of the video file")
    class validate {

        @ParameterizedTest
        @ValueSource(strings = {"video/mp4", "video/mpeg"})
        @DisplayName("Succeeds on validFileFormat")
        void succeedsOnValidFileFormat(String fileFormat) throws IOException {
            MockMultipartFile multipartFile =
                    new MockMultipartFile(NAME, FILE_NAME, fileFormat, "video".getBytes());
            Saver.save(multipartFile, DIR + fileId.toString());
            Assertions.assertEquals(true, FormatChecker.isFormatAccepted(multipartFile));
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
        @DisplayName("fails on validFileFormat")
        void failsOnInValidFileFormat(String fileFormat) throws IOException {
            MockMultipartFile multipartFile =
                    new MockMultipartFile(NAME, FILE_NAME, fileFormat, "video".getBytes());
            var result = Saver.save(multipartFile, DIR + fileId.toString());
            Assertions.assertEquals(false, FormatChecker.isFormatAccepted(multipartFile));
        }
    }
}
