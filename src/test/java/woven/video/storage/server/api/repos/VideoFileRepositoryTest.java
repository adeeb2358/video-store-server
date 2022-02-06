package woven.video.storage.server.api.repos;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;
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
import woven.video.storage.server.api.testutils.WithTestSetup;

@DisplayName("Video File Repository Test")
class VideoFileRepositoryTest extends WithTestSetup {

    @Autowired
    private VideoFileRepository repository;
    private MultipartFile multipartFile;
    private VideoFile videoFile;
    private static final String FILE_NAME = "test-video.mp4";
    private static final String NAME = "test-video";
    private static final String CONTENT_TYPE = "video/mp4";
    private static final String DIR = WithTestSetup.DIR;

    @BeforeEach
    void setRepository() throws IOException {
        multipartFile = new MockMultipartFile(NAME, FILE_NAME, CONTENT_TYPE, "video".getBytes());
        var checkSum = DigestUtils.md5DigestAsHex(multipartFile.getBytes());
        videoFile =
                VideoFile.builder()
                        .checkSum(checkSum)
                        .format(multipartFile.getContentType())
                        .createdAt(LocalDateTime.now())
                        .name(multipartFile.getOriginalFilename())
                        .size(String.valueOf(multipartFile.getSize()))
                        .build();
        videoFile =
                VideoFile.builder()
                        .checkSum(checkSum)
                        .format(multipartFile.getContentType())
                        .createdAt(LocalDateTime.now())
                        .name(multipartFile.getOriginalFilename())
                        .size(String.valueOf(multipartFile.getSize()))
                        .build();
        var result = repository.save(videoFile);
        result.setFilePath(DIR + result.getId());
        result.save(multipartFile);
        var savedResult = repository.save(result);
    }

    @DisplayName("Insert Documents to Video File Repository")
    @Nested
    class insert {

        @Test
        @DisplayName("Succeeds on saving one video information")
        void saveInformation() throws IOException {
            var multipartFile =
                    new MockMultipartFile(NAME, FILE_NAME, CONTENT_TYPE, "New_Video".getBytes());
            var checkSum = DigestUtils.md5DigestAsHex(multipartFile.getBytes());
            var videoFile =
                    VideoFile.builder()
                            .checkSum(checkSum)
                            .format(multipartFile.getContentType())
                            .createdAt(LocalDateTime.now())
                            .name(multipartFile.getOriginalFilename())
                            .size(String.valueOf(multipartFile.getSize()))
                            .build();
            var result = repository.save(videoFile);
            result.setFilePath(DIR + result.getId());
            result.save(multipartFile);
            var savedResult = repository.save(result);
            Assertions.assertEquals(result.getCheckSum(), videoFile.getCheckSum());
            Assertions.assertEquals(savedResult.getFilePath(), DIR + savedResult.getId());
            Assertions.assertEquals(savedResult.getName(), FILE_NAME);
            Assertions.assertEquals(savedResult.getFormat(), CONTENT_TYPE);
        }
    }

    @Nested
    @DisplayName("Remove a document from Video File Repository")
    class remove {

        @Test
        @DisplayName("Succeeds on removal")
        void succeedsOnRemoval() {
            Assertions.assertDoesNotThrow(
                    () -> repository.delete(repository.findById(videoFile.getId()).get()));
            Assertions.assertThrows(
                    NoSuchElementException.class,
                    () -> repository.findById(videoFile.getId()).get());
        }

        @Test
        @DisplayName("Fails on invalid Id")
        void failsOnInvalidId() {
            Assertions.assertThrows(
                    NoSuchElementException.class,
                    () -> repository
                            .delete(repository.findById(UUID.randomUUID().toString()).get()));
        }
    }

    @Nested
    @DisplayName("Get documents from Video File Repository")
    class get {

        @Test
        @DisplayName("Succeeds getting one Video Document")
        void succeedsGetOneVideoDocument() throws ParseException {
            var id = videoFile.getId();
            var searchResult = repository.findById(id).get();
            Assertions.assertEquals(id, searchResult.getId());
            validateFileContents(videoFile, searchResult);
        }

        @Test
        @DisplayName("Fails on getting invalid Id")
        void failsOnInvalidId() {
            Assertions.assertThrows(
                    NoSuchElementException.class,
                    () -> repository.findById(UUID.randomUUID().toString()).get());
        }

        @Test
        @DisplayName("Succeeds On get All files in the repository")
        void succeedsOnGetAll() throws ParseException {
            var result = repository.findAll();
            Assertions.assertEquals(1, repository.count());
            validateFileContents(result.get(0), videoFile);
        }

        @Test
        @DisplayName("Succeeds on finding file by checksum")
        void succeedsOnFindByCheckSum() throws ParseException {
            var searchResult = repository.findByCheckSum(videoFile.getCheckSum()).get();
            validateFileContents(videoFile, searchResult);
        }

        @Test
        @DisplayName("Fails on getting invalid checkSum")
        void failsOnInvalidCheckSum() {
            Assertions.assertThrows(
                    NoSuchElementException.class,
                    () -> repository.findByCheckSum("INVALID_CHECKSUM").get());
        }
    }

    private void validateFileContents(VideoFile videoFile, VideoFile searchResult)
            throws ParseException {
        Assertions.assertEquals(videoFile.getId(), searchResult.getId());
        Assertions.assertEquals(videoFile.getCheckSum(), searchResult.getCheckSum());
        Assertions.assertEquals(videoFile.getFilePath(), searchResult.getFilePath());
        Assertions.assertEquals(WithTestSetup.getDateTimeFormatted(videoFile.getCreatedAt()),
                WithTestSetup.getDateTimeFormatted(searchResult.getCreatedAt()));
        Assertions.assertEquals(videoFile.getBinaryContent(), searchResult.getBinaryContent());
        Assertions.assertEquals(videoFile.getName(), searchResult.getName());
        Assertions.assertEquals(videoFile.getFormat(), searchResult.getFormat());
    }
}
