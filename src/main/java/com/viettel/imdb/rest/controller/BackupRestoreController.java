package com.viettel.imdb.rest.controller;

import com.viettel.imdb.rest.domain.RestClientError;
import com.viettel.imdb.rest.model.BackupRequest;
import com.viettel.imdb.rest.model.ProcessStatus;
import com.viettel.imdb.rest.model.RestoreRequest;
import com.viettel.imdb.rest.service.BackupRestoreService;
import io.swagger.annotations.*;
import org.pmw.tinylog.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@Api(tags = "Backup/Restore related operations")
@RestController
@RequestMapping("/v1/tool")
public class BackupRestoreController {
    private static final String BACKUP_DATA_NOTES = "";
    private static final String RESTORE_DATA_NOTES = " ";
    private static final String BACKUP_DATA_STATUS_NOTES = "";
    private static final String RESTORE_DATA_STATUS_NOTES = " ";

    private static final String BACKUP_DATA_REQUEST_NOTES = "  ";
    private static final String RESTORE_DATA_REQUEST_NOTES = "   ";
    private static final String PROCESS_NOTES = " ";

    private final BackupRestoreService service;

    public BackupRestoreController(BackupRestoreService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/backup")
    @ApiOperation(value = BACKUP_DATA_NOTES, nickname = "backup")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
    })
    public DeferredResult<ResponseEntity<?>> backup(
            @ApiParam(required = true, value = BACKUP_DATA_REQUEST_NOTES) @RequestBody BackupRequest request
    ) {
        //BackupRequest request = new BackupRequest();
        Logger.info("BackupRequest({})", request);
        return service.backup(request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/backup")
    @ApiOperation(value = BACKUP_DATA_STATUS_NOTES, nickname = "processStatus")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public ProcessStatus backupProcessStatus(
            //@ApiParam(required = true, value = PROCESS_NOTES) @RequestParam(value = "process") int process
            @ApiParam(required = true, value = PROCESS_NOTES) @RequestParam(value = "process") String process
    ) {
        return service.backupProcessStatus(process);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restore")
    @ApiOperation(value = RESTORE_DATA_NOTES, nickname = "restore")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> restore(
            @ApiParam(required = true, value = RESTORE_DATA_REQUEST_NOTES) @RequestBody RestoreRequest request
    ) {
        return service.restore(request);
    }


    @RequestMapping(method = RequestMethod.GET, value = "restore")
    @ApiOperation(value = RESTORE_DATA_NOTES, nickname = "processStatus")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public ProcessStatus restoreprocessStatus(
            @ApiParam(required = true, value = PROCESS_NOTES) @RequestParam(value = "process") String process
            //@ApiParam(required = true, value = RESTORE_DATA_STATUS_NOTES) @PathVariable(value = "process") String process
    ) {
        return service.restoreProcessStatus(process);
    }
}
