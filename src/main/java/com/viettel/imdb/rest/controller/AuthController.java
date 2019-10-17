package com.viettel.imdb.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.viettel.imdb.rest.domain.RestClientError;
import com.viettel.imdb.rest.service.AuthService;
import io.swagger.annotations.*;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.annotations.ApiIgnore;

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

            @ApiParam(value = "user and password body")
            @RequestBody JsonNode body
    ) {
        System.out.println(body);
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>("{\"token\":  \"yIVBD5b73C75osbmwwshQNRC7frWUYrqaTjTpza2y4\"}", HttpStatus.CREATED));
        return returnValue;
        /*String username= requestParams.getFirst("username");
        String password = requestParams.getFirst("password");
        Logger.error("login({}, {})", username, password);
        return service.login(username, password);*/
    }
}
