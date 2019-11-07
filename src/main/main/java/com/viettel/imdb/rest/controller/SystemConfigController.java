package com.viettel.imdb.rest.controller;

import com.viettel.imdb.rest.domain.RestClientError;
import com.viettel.imdb.rest.model.EditUserRequest;
import com.viettel.imdb.rest.service.SystemConfigService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/v1/config")
public class SystemConfigController {
    private static final String USERNAME_NOTES = "";

    private static final String CONFIG_USER_NOTES = "";
    private static final String EDIT_USER_REQUEST_NOTES = "";
    private static final String SCAN_RESTORE_FILE = "";

    private static final String NODE = " ";
    private static final String BACKUP_DIR = " ";

    @Autowired
    private SystemConfigService service;

    @RequestMapping(method = RequestMethod.PATCH, value = "/user/{username}") // todo /user or /user/{username}
    @ApiOperation(value = CONFIG_USER_NOTES, nickname = "configUser")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> configUser(
            @ApiParam(required = true, value = CONFIG_USER_NOTES) @PathVariable(value = "username") String username,
            @ApiParam(required = true, value = EDIT_USER_REQUEST_NOTES) @RequestBody EditUserRequest editUserRequest
    ) {
        return service.currentUser(username, editUserRequest);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/restorefile")
    @ApiOperation(value = SCAN_RESTORE_FILE, nickname = "scan")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> getRestoreFile(
            @ApiParam(required = false, value = NODE) @RequestParam(value = "node") String node,
            @ApiParam(required = false, value = BACKUP_DIR) @RequestParam(value = "backupDir") String backupDir
    ) {
        return service.getRestoreFile(node, backupDir);


    }
}
