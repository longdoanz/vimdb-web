package com.viettel.imdb.rest.controller;

import com.viettel.imdb.rest.domain.RestClientError;
import com.viettel.imdb.rest.model.*;
import com.viettel.imdb.rest.service.SecurityService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * @author quannh22
 * @since 08/08/2019
 */

@Api(tags = "Security related operations", description = "Perform operations related to security")
@RestController
@RequestMapping("/v1/security")
public class SecurityController {
    private static final String GET_USERS_NOTES = "";
    private static final String GET_USER_NOTES = "";
    private static final String USERNAME_NOTES = "";
    private static final String ADD_USER_NOTES = "";
    private static final String ADD_USER_REQUEST_NOTES = "";
    private static final String EDIT_USER_NOTES = "";
    private static final String EDIT_USER_REQUEST_NOTES = "";
    private static final String DELETE_USER_NOTES = "";

    private static final String GET_ROLES_NOTES = "";
    private static final String GET_ROLE_NOTES = "";
    private static final String ROLENAME_NOTES = "";
    private static final String ADD_ROLE_NOTES = "";
    private static final String ADD_ROLE_REQUEST_NOTES = "";
    private static final String EDIT_ROLE_NOTES = "";
    private static final String EDIT_ROLE_REQUEST_NOTES = "";
    private static final String DELETE_ROLE_NOTES = "";
    /**"
     * Security Service to mainly serve request from this controller
     */
    @Autowired private SecurityService service;

    @RequestMapping(method = RequestMethod.GET, value = "/user")
    @ApiOperation(value = GET_USERS_NOTES, nickname = "getUsers")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    response = User.class,
                    responseContainer = "List",
                    message = "Key does not exist"
                    //examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            ),
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> getUsers() {
        return service.getUsers();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{username}")
    @ApiOperation(value = GET_USER_NOTES, nickname = "getUser")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    response = User.class,
                    message = "Key does not exist"
                    //examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            ),
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> getUser(
            @ApiParam(required = true, value = USERNAME_NOTES) @PathVariable(value = "username") String username
    ) {
        return service.getUser(username);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/{username}") // todo /user or /user/{username}
    @ApiOperation(value = ADD_USER_NOTES, nickname = "addUser")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    response = User.class,
                    responseContainer = "List",
                    message = "Key does not exist"
                    //examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            ),
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> addUser(
            @ApiParam(required = true, value = USERNAME_NOTES) @PathVariable(value = "username") String username,
            @ApiParam(required = true, value = ADD_USER_REQUEST_NOTES) @RequestBody AddUserRequest addUserRequest
    ) {
        addUserRequest.setUserName(username);
        return service.addUser(addUserRequest);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/user/{username}") // todo /user or /user/{username}
    @ApiOperation(value = EDIT_USER_NOTES, nickname = "editUser")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> addUser(
            @ApiParam(required = true, value = USERNAME_NOTES) @PathVariable(value = "username") String username,
            @ApiParam(required = true, value = EDIT_USER_REQUEST_NOTES) @RequestBody EditUserRequest editUserRequest
    ) {
        editUserRequest.setUserName(username);
        return service.editUser(editUserRequest);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{username}") // todo /user or /user/{username}
    @ApiOperation(value = DELETE_USER_NOTES, nickname = "deleteUser")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> deleteUser(
            @ApiParam(required = true, value = USERNAME_NOTES) @PathVariable(value = "username") String username
    ) {
        return service.deleteUser(username);
    }








    @RequestMapping(method = RequestMethod.GET, value = "/role")
    @ApiOperation(value = GET_ROLES_NOTES, nickname = "getRoles")
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
    public DeferredResult<ResponseEntity<?>> getRoles() {
        return service.getRoles();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/role/{rolename}")
    @ApiOperation(value = GET_ROLE_NOTES, nickname = "getRole")
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
    public DeferredResult<ResponseEntity<?>> getRole(
            @ApiParam(required = true, value = ROLENAME_NOTES) @PathVariable(value = "rolename") String rolename
    ) {
        return service.getRole(rolename);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/role/{rolename}") // todo /role or /role/{rolename}
    @ApiOperation(value = ADD_ROLE_NOTES, nickname = "addRole")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> addRole(
            @ApiParam(required = true, value = ROLENAME_NOTES) @PathVariable(value = "rolename") String rolename,
            @ApiParam(required = true, value = ADD_ROLE_REQUEST_NOTES) @RequestBody AddRoleRequest addRoleRequest
    ) {
        addRoleRequest.setRoleName(rolename);
        return service.addRole(addRoleRequest);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/role/{rolename}") // todo /role or /role/{rolename}
    @ApiOperation(value = EDIT_ROLE_NOTES, nickname = "editRole")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> addRole(
            @ApiParam(required = true, value = ROLENAME_NOTES) @PathVariable(value = "rolename") String rolename,
            @ApiParam(required = true, value = EDIT_ROLE_REQUEST_NOTES) @RequestBody EditRoleRequest editRoleRequest
    ) {
        editRoleRequest.setRoleName(rolename);
        return service.editRole(editRoleRequest);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/role/{rolename}") // todo /role or /role/{rolename}
    @ApiOperation(value = DELETE_ROLE_NOTES, nickname = "deleteRole")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> deleteRole(
            @ApiParam(required = true, value = ROLENAME_NOTES) @PathVariable(value = "rolename") String rolename
    ) {
        return service.deleteRole(rolename);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/audit-log")
    @ApiOperation(value = GET_ROLE_NOTES, nickname = "getAuditLogs")
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
    public DeferredResult<ResponseEntity<?>> getAuditLogs() {
        return service.getAuditLogs();
    }
}
