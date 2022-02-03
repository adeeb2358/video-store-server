package woven.video.storage.server.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
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
@RequestMapping(path = "files")
public class VideoFileController {

  private final VideoFileService service;

  private static List<VideoFileListView> toViewList(List<VideoFile> videoFileList) {
    return videoFileList.stream()
        .map(
            videoFile -> {
              return VideoFileListView.from(videoFile);
            })
        .collect(Collectors.toList());
  }

  @Operation(
      summary = "",
      description = "Upload a video file",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "File uploaded"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "409", description = "File exists"),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type")
      })
  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> create(
      @Parameter(description = "file detail") @Valid @RequestPart("file") MultipartFile file)
      throws IOException, InvalidFileFormatException {
    service.create(file);
    return VideoFileView.getCreateResponse();
  }

  @Operation(
      summary = "",
      description = "Delete a video file",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "File was successfully removed"),
        @ApiResponse(responseCode = "404", description = "File not found")
      })
  @DeleteMapping("/{fileid}")
  ResponseEntity<String> delete(
      @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema())
          @PathVariable("fileid")
          @NotBlank
          String fileid)
      throws IOException {
    service.delete(UUID.fromString(fileid));
    return VideoFileView.getDeleteResponse();
  }

  @Operation(
      description =
          "Download a video file by fileid. "
              + "The file name will be restored as it was when you uploaded it.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "OK",
            content =
                @Content(
                    mediaType = "video/mp4",
                    schema = @Schema(implementation = Resource.class))),
        @ApiResponse(responseCode = "404", description = "File not found")
      })
  @GetMapping(
      value = "/{fileid}",
      produces = {"video/mp4", "video/mpeg"})
  ResponseEntity<Resource> get(
      @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema())
          @PathVariable("fileid")
          String fileid)
      throws IOException {
    return VideoFileDownloadView.from(service.get(UUID.fromString(fileid)));
  }

  @Operation(
      summary = "",
      description = "List uploaded files",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "File list",
            content =
                @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(allOf = VideoFileListView.class))))
      })
  @RequestMapping(
      value = "/files",
      produces = {"application/json"},
      method = RequestMethod.GET)
  List<VideoFileListView> list() {
    return toViewList(service.list());
  }
}
