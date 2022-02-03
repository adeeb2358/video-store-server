package woven.video.storage.server.api.controllers.dtos.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import woven.video.storage.server.api.documents.VideoFile;

/** @author adeeb2358 */
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoFileListView {

  @JsonProperty("fileid")
  String id;

  @JsonProperty("name")
  String name;

  @JsonProperty("size")
  Integer size;

  @JsonProperty("created_at")
  String createdAt;

  public static VideoFileListView from(VideoFile file) {
    return VideoFileListView.builder()
        .id(file.getId())
        .name(file.getName())
        .size(Integer.getInteger(file.getSize()))
        .createdAt(file.getCreatedAt())
        .build();
  }
}
