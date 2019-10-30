package com.viettel.imdb.rest.controller;

import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.rest.domain.RestClientError;
import com.viettel.imdb.rest.model.AddUserRequest;
import com.viettel.imdb.rest.model.EditUserRequest;
import com.viettel.imdb.rest.model.RoleInfo;
import com.viettel.imdb.rest.model.UserInfo;
import com.viettel.imdb.rest.service.SecurityService;
import com.viettel.imdb.rest.util.IMDBClientToken;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
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


    @Autowired
    HttpServletRequest request;

    private String getToken() {
        return request.getHeader("Authorization");
    }
    
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
    public DeferredResult<ResponseEntity<?>> getUsers() {
        return service.getUsers(IMDBClientToken.getClient(getToken()));
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
    public DeferredResult<?> getUser(
            @ApiParam(required = true, value = USERNAME_NOTES) @PathVariable(value = "username") String username
    ) {
        return service.getUser(IMDBClientToken.getClient(getToken()), username);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user") // todo /user or /user/{username}
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
            @ApiParam(required = true, value = ADD_USER_REQUEST_NOTES) @RequestBody AddUserRequest addUserRequest
    ) {
        return service.addUser(IMDBClientToken.getClient(getToken()), addUserRequest);
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
            @ApiParam(required = true, value = USERNAME_NOTES) @PathVariable(value = "username") String username,
            @ApiParam(required = true, value = EDIT_USER_REQUEST_NOTES) @RequestBody EditUserRequest editUserRequest) {

        return service.editUser(IMDBClientToken.getClient(getToken()),username, editUserRequest);
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
    public DeferredResult<?> deleteUser(
            @ApiParam(required = true, value = USERNAME_NOTES) @PathVariable(value = "username") String username) {

        return service.deleteUser(IMDBClientToken.getClient(getToken()), username);
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
    public DeferredResult<List<Role>> getRoles() {

        return service.getRoles(IMDBClientToken.getClient(getToken()));
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
            @ApiParam(required = true, value = ROLENAME_NOTES) @PathVariable(value = "rolename") String rolename) {

        return service.getRole(IMDBClientToken.getClient(getToken()), rolename);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/role") // todo /role or /role/{rolename}
    @ApiOperation(value = ADD_ROLE_NOTES)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "role",
                    value = "{\n" +
                            "  \"name\": \"ROLE01\",\n" +
                            "  \"privileges\": [{\n" +
                            "    \"permission\": \"read\",\n" +
                            "    \"resource\": {\n" +
                            "      \"name\": \"user\",\n" +
                            "      \"user\": \"*\"\n" +
                            "    }\n" +
                            "  }]\n" +
                            "}")
    })
    public DeferredResult<ResponseEntity<?>> addRole(
            @ApiParam(required = true) @RequestBody Role role) {

        return service.addRole(IMDBClientToken.getClient(getToken()), role);
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
            @ApiParam(required = true, value = ROLENAME_NOTES) @PathVariable(value = "rolename") String rolename,
            @ApiParam(required = true, value = EDIT_ROLE_REQUEST_NOTES) @RequestBody Role editRoleRequest) {

        return service.editRole(IMDBClientToken.getClient(getToken()),rolename,  editRoleRequest);
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
            @ApiParam(required = true, value = ROLENAME_NOTES) @PathVariable(value = "rolename") String rolename) {

        return service.deleteRole(IMDBClientToken.getClient(getToken()), rolename);
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
    public DeferredResult<ResponseEntity<?>> getAuditLogs() {
        return service.getAuditLogs(IMDBClientToken.getClient(getToken()));
    }
}
