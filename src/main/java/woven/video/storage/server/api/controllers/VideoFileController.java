package woven.video.storage.server.api.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import woven.video.storage.server.api.controllers.dtos.views.VideoFileDownloadView;
import woven.video.storage.server.api.controllers.dtos.views.VideoFileListView;
import woven.video.storage.server.api.controllers.dtos.views.VideoFileView;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.services.VideoFileService;
import woven.video.storage.server.api.services.impl.VideoFileServiceImpl.InvalidFileFormatException;

/** @author adeeb2358 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/files")
public class VideoFileController implements VideoFileApi {

  private final VideoFileService service;

  private static List<VideoFileListView> toViewList(List<VideoFile> videoFileList) {
    return videoFileList.stream()
        .map(
            videoFile -> {
              return VideoFileListView.from(videoFile);
            })
        .collect(Collectors.toList());
  }

  @Override
  public ResponseEntity<String> create(
      @Parameter(description = "file detail") @Valid @RequestPart("file") MultipartFile file)
      throws IOException, InvalidFileFormatException {
    return VideoFileView.createResponse(service.create(file));
  }

  @Override
  public ResponseEntity<String> delete(
      @Parameter(in = ParameterIn.PATH, required = true) @PathVariable("fileid") @NotBlank
          String fileid)
      throws IOException {
    return VideoFileView.deleteResponse(service.delete(fileid));
  }

  @Override
  public ResponseEntity<Resource> get(
      @Parameter(in = ParameterIn.PATH, required = true) @PathVariable("fileid") String fileid)
      throws IOException {
    return VideoFileDownloadView.from(service.get(fileid));
  }

  @Override
  public List<VideoFileListView> list() {
    return toViewList(service.list());
  }
}
