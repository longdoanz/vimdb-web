package com.viettel.imdb.rest.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author quannh22
 * @since 03/10/2019
 */
public interface AuthService {
    DeferredResult<ResponseEntity<?>> login(String username, String password);
    DeferredResult<ResponseEntity<?>> createAuthenticationToken(String username, String password);
    public String getUsernameFromeToken(String token);
}
