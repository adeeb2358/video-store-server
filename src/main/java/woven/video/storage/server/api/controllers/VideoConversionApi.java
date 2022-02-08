package woven.video.storage.server.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.FileNotFoundException;
import java.util.List;
import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import woven.video.storage.server.api.controllers.dtos.views.conversions.FileConversionGetView.OperationNotCompletedException;
import woven.video.storage.server.api.controllers.dtos.views.conversions.FileConversionListView;

/**
 * @author adeeb2358
 */
public interface VideoConversionApi {

    @Operation(
            summary = "Convert a video file",
            description = "Convert the video file to webm format",
            tags = {"file-conversion"})
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Conversion started",
                            content = {
                                    @Content(
                                            mediaType = "text/plain; charset=us-ascii",
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Response Message",
                                                            value = "File Added to the conversion"
                                                                    + " Queue"
                                                                    + ". "
                                                                    + "fileId"
                                                                    + ":61fd4a7a4210464bb110f0f7")
                                            })
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = {
                                    @Content(
                                            mediaType = "text/plain; charset=us-ascii",
                                            examples = {
                                                    @ExampleObject(name = "Response Message",
                                                            value = "Bad Request")})
                            }),
                    @ApiResponse(
                            responseCode = "404",
                            description = "File does not exists",
                            content = {
                                    @Content(
                                            mediaType = "text/plain; charset=us-ascii",
                                            examples = {
                                                    @ExampleObject(name = "Response Message",
                                                            value = "File doesnot exists")})
                            }),
                    @ApiResponse(
                            responseCode = "200",
                            description = "Conversion finished",
                            content = {
                                    @Content(
                                            mediaType = "text/plain; charset=us-ascii",
                                            examples = {
                                                    @ExampleObject(name = "Response Message",
                                                            value = "Conversion finished")
                                            })
                            })
            })
    public ResponseEntity<String> create(
            @Parameter(in = ParameterIn.PATH, required = true)
            @PathVariable("fileid") @NotBlank String fileId)
            throws FileNotFoundException, OperationNotSupportedException,
            OperationNotCompletedException, InterruptedException;

    @Operation(
            summary = "Download a converted video file",
            description = "Download a converted video file",
            tags = {"file-conversion"})
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Conversion started",
                            content = {
                                    @Content(
                                            mediaType = "text/plain; charset=us-ascii",
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Response Message",
                                                            value = "File Added to the conversion"
                                                                    + " Queue"
                                                                    + ". "
                                                                    + "fileId"
                                                                    + ":61fd4a7a4210464bb110f0f7")
                                            })
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = {
                                    @Content(
                                            mediaType = "text/plain; charset=us-ascii",
                                            examples = {
                                                    @ExampleObject(name = "Response Message",
                                                            value = "Bad Request")})
                            }),
                    @ApiResponse(
                            responseCode = "404",
                            description = "File does not exists",
                            content = {
                                    @Content(
                                            mediaType = "text/plain; charset=us-ascii",
                                            examples = {
                                                    @ExampleObject(name = "Response Message",
                                                            value = "File doesnot exists")})
                            }),
                    @ApiResponse(
                            responseCode = "200",
                            description = "Conversion finished",
                            content = {
                                    @Content(
                                            mediaType = "BlobFile",
                                            examples = {
                                                    @ExampleObject(name = "binary file", value =
                                                            "blob webm file")
                                            })
                            })
            })
    public ResponseEntity<Resource> get(
            @Parameter(in = ParameterIn.PATH, required = true)
            @PathVariable("fileid") @NotBlank
                    String fileId)
            throws FileNotFoundException, OperationNotSupportedException,
            OperationNotCompletedException;

    @Operation(
            summary = "List the information of all files passed for conversion",
            description = "Details of conversion status, file created and last updated date-time "
                    + "etc",
            tags = {"file-conversion"})
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Conversion started",
                            content = {
                                    @Content(
                                            mediaType = "text/plain; charset=us-ascii",
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Response Message",
                                                            value = "[{\"fileid"
                                                                    +
                                                                    "\":\"62026d24906de"
                                                                    + "e32ce745503\""
                                                                    + ",\"progress\":100.0,"
                                                                    + "\"status\":\"FINISHED\","
                                                                    + "\"name\":\"test-file"
                                                                    + ".webm\","
                                                                    + "\"started_at\":\"2022-02"
                                                                    + "-08 14:05:14\","
                                                                    + "\"last_updated_at\""
                                                                    + ":\"2022-02-08 14:05:33\"}]")
                                            }, schema = @Schema(implementation = Resource.class)
                                    )
                            }),
            })
    public List<FileConversionListView> list();

    @Operation(
            summary = "ReConvert a failed video file",
            description = "Convert the video file to webm format",
            tags = {"file-conversion"})
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Conversion started",
                            content = {
                                    @Content(
                                            mediaType = "text/plain; charset=us-ascii",
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Response Message",
                                                            value = "File Added to the conversion"
                                                                    + " Queue"
                                                                    + ". "
                                                                    + "fileId"
                                                                    + ":61fd4a7a4210464bb110f0f7")
                                            })
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = {
                                    @Content(
                                            mediaType = "text/plain; charset=us-ascii",
                                            examples = {
                                                    @ExampleObject(name = "Response Message",
                                                            value = "Bad Request")})
                            }),
                    @ApiResponse(
                            responseCode = "404",
                            description = "File does not exists",
                            content = {
                                    @Content(
                                            mediaType = "text/plain; charset=us-ascii",
                                            examples = {
                                                    @ExampleObject(name = "Response Message",
                                                            value = "File doesnot exists")})
                            }),
                    @ApiResponse(
                            responseCode = "200",
                            description = "Conversion finished",
                            content = {
                                    @Content(
                                            mediaType = "text/plain; charset=us-ascii",
                                            examples = {
                                                    @ExampleObject(name = "Response Message",
                                                            value = "Conversion finished")
                                            })
                            })
            })
    public ResponseEntity<String> update(
            @Parameter(in = ParameterIn.PATH, required = true)
            @PathVariable("fileid") @NotBlank
                    String fileId)
            throws FileNotFoundException, OperationNotSupportedException,
            OperationNotCompletedException, InterruptedException;
}
