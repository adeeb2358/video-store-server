package video.storage.server.api.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;
import video.storage.server.api.utils.file.Remover;
import video.storage.server.api.utils.file.Saver;

/**
 * The type Video file.
 *
 * @author adeeb2358
 */
@Document
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoFile {

    @Id
    String id;

    @TextIndexed
    @NotBlank String name;

    @NotNull String size;

    @NotNull String format;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    LocalDateTime createdAt;

    @NotNull String checkSum;

    @NotNull String filePath;

    @Builder.Default
    @NotNull Status status = Status.NOT_CONVERTED;

    @NotNull Double progress;

    @NotNull LocalDateTime conversionStartTime;

    @NotNull LocalDateTime conversionLastUpdated;

    @NotNull String failureMessage;

    /**
     * Conversion queued.
     */
    public void conversionQueued() {
        this.conversionStartTime = LocalDateTime.now();
        this.progress = 0.0;
        this.status = Status.QUEUED;
    }

    /**
     * Conversion in progress.
     *
     * @param progress the progress
     */
    public void conversionInProgress(Double progress) {
        this.status = Status.IN_PROGRESS;
        this.progress = progress;
        this.conversionLastUpdated = LocalDateTime.now();
    }

    /**
     * Conversion finished.
     */
    public void conversionFinished() {
        this.status = Status.FINISHED;
        this.progress = 100.0;
        this.conversionLastUpdated = LocalDateTime.now();
    }

    /**
     * Conversion failed.
     *
     * @param message the message
     */
    public void conversionFailed(String message) {
        this.status = Status.FAILED;
        this.conversionLastUpdated = LocalDateTime.now();
        this.failureMessage = message;
    }

    /**
     * Delete.
     *
     * @throws IOException the io exception
     */
    public void delete() throws IOException {
        Remover.remove(this.filePath);
        if (!this.getStatus().equals(Status.NOT_CONVERTED)) {
            Remover.remove(this.getConvertedFilePath());
        }
    }

    /**
     * Save.
     *
     * @param file the file
     * @throws IOException the io exception
     */
    public void save(MultipartFile file) throws IOException {
        this.filePath = Saver.save(file, this.filePath);
        this.setCreatedAt(LocalDateTime.now());
    }

    /**
     * Is exists boolean.
     *
     * @return the boolean
     */
    public boolean isExists() {
        return new File(this.filePath).exists();
    }

    /**
     * Gets converted file path.
     *
     * @return the converted file path
     */
    public String getConvertedFilePath() {
        var dir = Path.of(this.getFilePath()).getParent().toString();
        var path = dir + "/converted/" + this.getId().toString();
        return path;
    }

    /**
     * Gets converted content.
     *
     * @return the converted content
     */
    public File getConvertedContent() {
        return new File(this.getConvertedFilePath());
    }

    /**
     * Gets binary content.
     *
     * @return the binary content
     */
    public File getBinaryContent() {
        return new File(this.filePath);
    }

    /**
     * Gets converted name.
     *
     * @return the converted name
     */
    public String getConvertedName() {
        if (this.name.indexOf(".") > 0) {
            name = name.substring(0, name.lastIndexOf("."));
        }
        return name + "." + "webm";
    }

    /**
     * The enum Status.
     */
    public enum Status {
        /**
         * Not converted status.
         */
        NOT_CONVERTED,
        /**
         * Queued status.
         */
        QUEUED,
        /**
         * In progress status.
         */
        IN_PROGRESS,
        /**
         * Failed status.
         */
        FAILED,
        /**
         * Finished status.
         */
        FINISHED
    }
}
