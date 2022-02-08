package woven.video.storage.server.api.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woven.video.storage.server.api.controllers.dtos.views.conversions.FileConversionGetView;
import woven.video.storage.server.api.controllers.dtos.views.conversions.FileConversionGetView.OperationNotCompletedException;
import woven.video.storage.server.api.controllers.dtos.views.conversions.FileConversionListView;
import woven.video.storage.server.api.controllers.dtos.views.conversions.FileConversionView;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.services.VideoConversionService;

/**
 * @author adeeb2358
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/file-conversion")
public class VideoConversionController {

    private final VideoConversionService service;

    private static List<FileConversionListView> toViewList(List<VideoFile> videoFileList) {
        return videoFileList.stream()
                .map(
                        videoFile -> {
                            return FileConversionListView.from(videoFile);
                        })
                .collect(Collectors.toList());
    }

    @PostMapping(
            value = "/{fileid}")
    public ResponseEntity<String> create(
            @Parameter(in = ParameterIn.PATH, required = true)
            @PathVariable("fileid") @NotBlank String fileId)
            throws FileNotFoundException, OperationNotSupportedException,
            OperationNotCompletedException, InterruptedException {
        return FileConversionView.from(service.create(fileId));
    }

    @GetMapping(
            value = "/{fileid}")
    public ResponseEntity<Resource> get(
            @Parameter(in = ParameterIn.PATH, required = true)
            @PathVariable("fileid") @NotBlank
                    String fileId)
            throws FileNotFoundException, OperationNotSupportedException,
            OperationNotCompletedException {
        return FileConversionGetView.from(service.get(fileId));
    }

    @GetMapping(value = "",
            produces = {"application/json"})
    public List<FileConversionListView> list() {
        return toViewList(service.list());
    }

    @PutMapping(value = "/{fileid}", produces = {"application/json"})
    public ResponseEntity<String> update(
            @Parameter(in = ParameterIn.PATH, required = true)
            @PathVariable("fileid") @NotBlank
                    String fileId)
            throws FileNotFoundException, OperationNotSupportedException,
            OperationNotCompletedException, InterruptedException {
        return FileConversionView.from(service.convertFailed(fileId));
    }
}
