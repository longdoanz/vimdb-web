package com.viettel.imdb.rest.service;

import com.viettel.imdb.IMDBClient;
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
    DeferredResult<ResponseEntity<?>> getUsers(IMDBClient client);

    DeferredResult<ResponseEntity<?>> getUser(IMDBClient client, String username);

    DeferredResult<ResponseEntity<?>> addUser(IMDBClient client, AddUserRequest addUserRequest);

    DeferredResult<ResponseEntity<?>> editUser(IMDBClient client, EditUserRequest editUserRequest);

    DeferredResult<ResponseEntity<?>> deleteUser(IMDBClient client, String username);

    DeferredResult<ResponseEntity<?>> getRoles(IMDBClient client);

    DeferredResult<ResponseEntity<?>> getRole(IMDBClient client, String username);

    DeferredResult<ResponseEntity<?>> addRole(IMDBClient client, AddRoleRequest addRoleRequest);

    DeferredResult<ResponseEntity<?>> editRole(IMDBClient client, EditRoleRequest editRoleRequest);

    DeferredResult<ResponseEntity<?>> deleteRole(IMDBClient client, String username);

    DeferredResult<ResponseEntity<?>> getAuditLogs(IMDBClient client);
}
