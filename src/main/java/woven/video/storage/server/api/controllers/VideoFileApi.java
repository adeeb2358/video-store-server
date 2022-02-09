package woven.video.storage.server.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import woven.video.storage.server.api.controllers.dtos.views.files.FileListView;
import woven.video.storage.server.api.services.impl.VideoFileServiceImpl.InvalidFileFormatException;

/**
 * The interface Video file api.
 *
 * @author adeeb2358
 */
public interface VideoFileApi {

    /**
     * Create response entity.
     *
     * @param file the file
     * @return the response entity
     * @throws IOException                the io exception
     * @throws InvalidFileFormatException the invalid file format exception
     */
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(
      summary = "Upload a video file",
      description = "Upload video to the video store",
      tags = {"file-store"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "File uploaded",
            content = {
              @Content(
                  mediaType = "text/plain; charset=us-ascii",
                  examples = {
                    @ExampleObject(
                        name = "Response Message",
                        value = "File Uploaded Successfully. fileId:61fd4a7a4210464bb110f0f7")
                  })
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = {
              @Content(
                  mediaType = "text/plain; charset=us-ascii",
                  examples = {@ExampleObject(name = "Response Message", value = "Bad Request")})
            }),
        @ApiResponse(
            responseCode = "409",
            description = "File exists",
            content = {
              @Content(
                  mediaType = "text/plain; charset=us-ascii",
                  examples = {@ExampleObject(name = "Response Message", value = "File exists")})
            }),
        @ApiResponse(
            responseCode = "415",
            description = "Unsupported Media Type",
            content = {
              @Content(
                  mediaType = "text/plain; charset=us-ascii",
                  examples = {
                    @ExampleObject(name = "Response Message", value = "Unsupported Media Type")
                  })
            })
      })
  @ResponseStatus(HttpStatus.CREATED)
  ResponseEntity<String> create(
      @Parameter(description = "file detail") @Valid @RequestPart("file") MultipartFile file)
      throws IOException, InvalidFileFormatException;

    /**
     * Delete response entity.
     *
     * @param fileid the fileid
     * @return the response entity
     * @throws IOException the io exception
     */
    @DeleteMapping("/{fileid}")
  @Operation(
      summary = "Remove an uploaded video file from the server",
      description = "Delete a video file",
      tags = {"file-store"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "File Removed Successfully",
            content = {
              @Content(
                  mediaType = "text/plain; charset=us-ascii",
                  examples = {
                    @ExampleObject(
                        name = "Response Message",
                        value = "File Removed Successfully. fileId:61fd4a7a4210464bb110f0f7")
                  })
            }),
        @ApiResponse(
            responseCode = "404",
            description = "File not found",
            content = {
              @Content(
                  mediaType = "text/plain; charset=us-ascii",
                  examples = {@ExampleObject(name = "Response Message", value = "File Not Found")})
            })
      })
  ResponseEntity<String> delete(
      @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema())
          @PathVariable("fileid")
          @NotBlank
          String fileid)
      throws IOException;

    /**
     * Get response entity.
     *
     * @param fileid the fileid
     * @return the response entity
     * @throws IOException the io exception
     */
    @Operation(
      summary =
          "Download a video file by fileid.The file "
              + "name will be restored as it was when you uploaded it. ",
      description = "Download a video file",
      tags = {"file-store"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = {
              @Content(
                  mediaType = "video/mp4",
                  examples = {@ExampleObject(name = "List of Items", value = "Binary Video File")},
                  schema = @Schema(implementation = Resource.class)),
              @Content(
                  mediaType = "video/mpeg",
                  examples = {@ExampleObject(name = "List of Items", value = "Binary Video File")},
                  schema = @Schema(implementation = Resource.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "File not found",
            content = {
              @Content(
                  mediaType = "text/plain; charset=us-ascii",
                  examples = {@ExampleObject(name = "Response Message", value = "File not found")})
            })
      })
  @GetMapping(
      value = "/{fileid}",
      produces = {"video/mp4", "video/mpeg"})
  ResponseEntity<Resource> get(
      @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema())
          @PathVariable("fileid")
          String fileid)
      throws IOException;

    /**
     * List list.
     *
     * @return the list
     */
    @Operation(
      summary = "List all the uploaded files in the ",
      description = "List uploaded files",
      tags = {"file-store"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "File list",
            content =
                @Content(
                    mediaType = "application/json",
                    examples = {
                      @ExampleObject(
                          name = "List of Items",
                          value =
                              "[{\"fileid\":"
                                  + "\"61fd4a7a4210464bb110f0f7\",\"name\":"
                                  + "\"sample-5s.mp4\",\"size\":\"2848208\","
                                  + "\"created_at\":\"2022-02-05 22:12:12\"}]")
                    },
                    array = @ArraySchema(schema = @Schema(allOf = FileListView.class))))
      })
  @RequestMapping(
      value = "",
      produces = {"application/json"},
      method = RequestMethod.GET)
  List<FileListView> list();
}
