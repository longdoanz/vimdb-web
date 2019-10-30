package com.viettel.imdb.rest.service;

import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.rest.model.AddUserRequest;
import com.viettel.imdb.rest.model.EditUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * @author quannh22
 * @since 17/09/2019
 */
public interface SecurityService {
    DeferredResult<ResponseEntity<?>> getUsers(IMDBClient client);

    DeferredResult<?> getUser(IMDBClient client, String username);

    DeferredResult<ResponseEntity<?>> addUser(IMDBClient client,AddUserRequest addUserRequest);

    DeferredResult<ResponseEntity<?>> editUser(IMDBClient client,String username, EditUserRequest editUserRequest);

    DeferredResult<?> deleteUser(IMDBClient client, String username);

    DeferredResult<List<Role>> getRoles(IMDBClient client);

    DeferredResult<ResponseEntity<?>> getRole(IMDBClient client, String username);

    DeferredResult<ResponseEntity<?>> addRole(IMDBClient client, Role role);

    DeferredResult<ResponseEntity<?>> editRole(IMDBClient client,String roleName, Role editRoleRequest);

    DeferredResult<ResponseEntity<?>> deleteRole(IMDBClient client, String username);

    DeferredResult<ResponseEntity<?>> getAuditLogs(IMDBClient client);
}
