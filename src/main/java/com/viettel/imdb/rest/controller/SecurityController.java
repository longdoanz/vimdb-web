package com.viettel.imdb.rest.controller;

import com.viettel.imdb.rest.domain.RestClientError;
import com.viettel.imdb.rest.model.*;
import com.viettel.imdb.rest.service.SecurityService;
import com.viettel.imdb.rest.util.IMDBClientToken;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

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
                    response = UserInfo.class,
                    responseContainer = "List",
                    message = "OK"
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
    public DeferredResult<ResponseEntity<?>> getUsers(
            @RequestHeader("Authorization") String token
    ) {
        return service.getUsers(IMDBClientToken.getClient(token));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{username}")
    @ApiOperation(value = GET_USER_NOTES, nickname = "getUser")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    response = UserInfo.class,
                    message = "OK"
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
            @RequestHeader("Authorization") String token,
            @ApiParam(required = true, value = USERNAME_NOTES) @PathVariable(value = "username") String username
    ) {
        return service.getUser(IMDBClientToken.getClient(token), username);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/{username}") // todo /user or /user/{username}
    @ApiOperation(value = ADD_USER_NOTES, nickname = "addUser")
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
    public DeferredResult<ResponseEntity<?>> addUser(
            @RequestHeader("Authorization") String token,
            @ApiParam(required = true, value = USERNAME_NOTES) @PathVariable(value = "username") String username,
            @ApiParam(required = true, value = ADD_USER_REQUEST_NOTES) @RequestBody AddUserRequest addUserRequest
    ) {
        addUserRequest.setUserName(username);
        return service.addUser(IMDBClientToken.getClient(token), addUserRequest);
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
    public DeferredResult<ResponseEntity<?>> editUser(
            @RequestHeader("Authorization") String token,
            @ApiParam(required = true, value = USERNAME_NOTES) @PathVariable(value = "username") String username,
            @ApiParam(required = true, value = EDIT_USER_REQUEST_NOTES) @RequestBody EditUserRequest editUserRequest
    ) {
        editUserRequest.setUserName(username);
        return service.editUser(IMDBClientToken.getClient(token), editUserRequest);
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
            @RequestHeader("Authorization") String token,
            @ApiParam(required = true, value = USERNAME_NOTES) @PathVariable(value = "username") String username
    ) {
        return service.deleteUser(IMDBClientToken.getClient(token), username);
    }








    @RequestMapping(method = RequestMethod.GET, value = "/role")
    @ApiOperation(value = GET_ROLES_NOTES, nickname = "getRoles")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    response = RoleInfo.class,
                    message = "OK",
                    responseContainer = "List"
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
    public DeferredResult<ResponseEntity<?>> getRoles(
            @RequestHeader("Authorization") String token
    ) {
        return service.getRoles(IMDBClientToken.getClient(token));
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
            @RequestHeader("Authorization") String token,
            @ApiParam(required = true, value = ROLENAME_NOTES) @PathVariable(value = "rolename") String rolename
    ) {
        return service.getRole(IMDBClientToken.getClient(token), rolename);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/role") // todo /role or /role/{rolename}
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
            @RequestHeader("Authorization") String token,
            @ApiParam(required = true, value = ADD_ROLE_REQUEST_NOTES) @RequestBody AddRoleRequest addRoleRequest
    ) {
        return service.addRole(IMDBClientToken.getClient(token), addRoleRequest);
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
    public DeferredResult<ResponseEntity<?>> editRole(
            @RequestHeader("Authorization") String token,
            @ApiParam(required = true, value = ROLENAME_NOTES) @PathVariable(value = "rolename") String rolename,
            @ApiParam(required = true, value = EDIT_ROLE_REQUEST_NOTES) @RequestBody EditRoleRequest editRoleRequest
    ) {
        editRoleRequest.setRoleName(rolename);
        return service.editRole(IMDBClientToken.getClient(token), editRoleRequest);
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
            @RequestHeader("Authorization") String token,
            @ApiParam(required = true, value = ROLENAME_NOTES) @PathVariable(value = "rolename") String rolename
    ) {
        return service.deleteRole(IMDBClientToken.getClient(token), rolename);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/audit-log")
    @ApiOperation(value = GET_ROLE_NOTES, nickname = "getAuditLogs")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
//                    response = AuditLog.class,
                    message = "OK",
                    responseContainer = "List"
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
    public DeferredResult<ResponseEntity<?>> getAuditLogs(
            @RequestHeader("Authorization") String token
    ) {
        return service.getAuditLogs(IMDBClientToken.getClient(token));
    }
}
