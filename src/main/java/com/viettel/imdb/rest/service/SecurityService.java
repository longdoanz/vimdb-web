package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.model.AddRoleRequest;
import com.viettel.imdb.rest.model.AddUserRequest;
import com.viettel.imdb.rest.model.EditRoleRequest;
import com.viettel.imdb.rest.model.EditUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author quannh22
 * @since 17/09/2019
 */
public interface SecurityService {
    DeferredResult<ResponseEntity<?>> getUsers();

    DeferredResult<ResponseEntity<?>> getUser(String username);

    DeferredResult<ResponseEntity<?>> addUser(AddUserRequest addUserRequest);

    DeferredResult<ResponseEntity<?>> editUser(EditUserRequest editUserRequest);

    DeferredResult<ResponseEntity<?>> deleteUser(String username);

    DeferredResult<ResponseEntity<?>> getRoles();

    DeferredResult<ResponseEntity<?>> getRole(String username);

    DeferredResult<ResponseEntity<?>> addRole(AddRoleRequest addRoleRequest);

    DeferredResult<ResponseEntity<?>> editRole(EditRoleRequest editRoleRequest);

    DeferredResult<ResponseEntity<?>> deleteRole(String username);

    DeferredResult<ResponseEntity<?>> getAuditLogs();
}
