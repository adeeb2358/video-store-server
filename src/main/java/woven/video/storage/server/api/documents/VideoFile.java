package woven.video.storage.server.api.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.io.File;
import java.io.IOException;
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
import woven.video.storage.server.api.utils.file.Remover;
import woven.video.storage.server.api.utils.file.Saver;

/**
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
    @NotNull LocalDateTime createdAt;

    @NotNull String checkSum;

    @NotNull String filePath;

    public void delete() throws IOException {
        Remover.remove(this.filePath);
    }

    public void save(MultipartFile file) throws IOException {
        this.filePath = Saver.save(file, this.filePath);
        this.setCreatedAt(LocalDateTime.now());
    }

    public boolean isExists() {
        return new File(this.filePath).exists();
    }

    public File getBinaryContent() {
        return new File(this.filePath);
    }
}
