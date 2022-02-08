package woven.video.storage.server.api.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.io.FileNotFoundException;
import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import woven.video.storage.server.api.controllers.dtos.views.conversions.FileConversionView.OperationNotCompletedException;

public interface VideoConversionApi {

    public ResponseEntity<Resource> create(
            @Parameter(in = ParameterIn.PATH, required = true)
            @PathVariable("fileid") @NotBlank String fileId)
            throws FileNotFoundException, OperationNotSupportedException,
            OperationNotCompletedException, InterruptedException;
}
