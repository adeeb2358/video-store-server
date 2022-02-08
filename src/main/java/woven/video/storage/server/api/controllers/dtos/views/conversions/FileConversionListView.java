package woven.video.storage.server.api.controllers.dtos.views.conversions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.documents.VideoFile.Status;

/**
 * @author adeeb2358
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileConversionListView {

    @JsonProperty("fileid")
    String id;

    @JsonProperty("progress")
    Double progress;

    @JsonProperty("status")
    Status status;

    @JsonProperty("name")
    String name;

    @JsonProperty("started_at")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime started_at;

    @JsonProperty("last_updated_at")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updated_at;

    public static FileConversionListView from(VideoFile file) {
        return FileConversionListView.builder()
                .id(file.getId())
                .name(file.getConvertedName())
                .progress(file.getProgress())
                .status(file.getStatus())
                .started_at(file.getConversionStartTime())
                .updated_at(file.getConversionLastUpdated())
                .build();
    }
}
