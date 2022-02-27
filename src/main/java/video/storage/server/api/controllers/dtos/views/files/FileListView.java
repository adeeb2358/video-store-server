package video.storage.server.api.controllers.dtos.views.files;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import video.storage.server.api.documents.VideoFile;

/**
 * The type File list view.
 *
 * @author adeeb2358
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileListView {

    @JsonProperty("fileid")
    String id;

    @JsonProperty("name")
    String name;

    @JsonProperty("size")
    String size;

    @JsonProperty("created_at")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    /**
     * From file list view.
     *
     * @param file the file
     * @return the file list view
     */
    public static FileListView from(VideoFile file) {
        return FileListView.builder()
                .id(file.getId())
                .name(file.getName())
                .size(file.getSize())
                .createdAt(file.getCreatedAt())
                .build();
    }

}
