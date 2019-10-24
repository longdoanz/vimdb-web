package com.viettel.imdb.rest.controller;

import com.viettel.imdb.rest.model.AuthenRequest;
import com.viettel.imdb.rest.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author quannh22
 * @since 04/10/2019
 */
@Api(tags = "Web client sign on related operations")
@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private static final String LOGIN_NOTES = "Login a client in to this system and create the corresponding imdb-client, return a token for later use";
    private static final String USERNAME_NOTES = "Input username";
    private static final String PASSWORD_NOTES = "Input password";
    private static final String USERNAME_AND_PASSWORD_NOTES = "Input username and password";
    private static final String AUTHEN_REQUEST_NOTES = "Username and password request";
    /**
     ************************************************
     *             CONSTANTS COME HERE              *
     ************************************************
     */

    // todo password as String @PathVariable or byte[] @RequestBody
    @Autowired private AuthService service;

    @RequestMapping(method = RequestMethod.POST, value = "/login") // todo why not work with "/" only :-?
    @ApiOperation(value = LOGIN_NOTES, nickname = "login")
    @ResponseStatus(HttpStatus.OK)
    public DeferredResult<ResponseEntity<?>> login(
//        @ApiParam(required = false, value = USERNAME_NOTES) @PathVariable(value = "username") String username,
//        @ApiParam(required = false, value = PASSWORD_NOTES) @PathVariable(value = "password") String password,
//            @RequestParam(value = "username") String username,
//            @RequestParam(value = "password") String password

            //@RequestParam MultiValueMap<String, String> requestParams
            @ApiParam(required = true, value = AUTHEN_REQUEST_NOTES) @RequestBody AuthenRequest request
    ) {
        String username= request.getUsername();
        String password = request.getPassword();
        Logger.info("login({}, {})", username, password);

        return service.login(username, password);
    }


    /*@RequestMapping(method = RequestMethod.POST, value = "/authenticate") // todo why not work with "/" only :-?
    @ApiOperation(value = LOGIN_NOTES, nickname = "authenticate")
    @ResponseStatus(HttpStatus.OK)
    public DeferredResult<ResponseEntity<?>> createAuthenticationToken(
            @ApiParam(required = true, value = AUTHEN_REQUEST_NOTES) @RequestBody AuthenRequest request
    ){

        String username= request.getUsername();
        String password = request.getPassword();
        Logger.info("login authen({}, {})", username, password);
        return service.createAuthenticationToken(username, password);
    }*/
}
