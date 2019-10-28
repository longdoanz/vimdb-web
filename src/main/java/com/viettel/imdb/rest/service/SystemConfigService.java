package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.model.EditUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface SystemConfigService {
    DeferredResult<ResponseEntity<?>> currentUser(String username, EditUserRequest request);
    DeferredResult<ResponseEntity<?>> getRestoreFile(String node, String bacupDir);

}
