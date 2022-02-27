package video.storage.server.api.controllers.dtos.views.files;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import video.storage.server.api.documents.VideoFile;

/**
 * The type File download view.
 *
 * @author adeeb2358
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDownloadView {

    /**
     * From response entity.
     *
     * @param file the file
     * @return the response entity
     */
    public static ResponseEntity<Resource> from(VideoFile file) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getName() + "\"")
                .body(new FileSystemResource(file.getBinaryContent()));
    }
}
