package com.viettel.imdb.rest.service;

import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.rest.model.AddUserRequest;
import com.viettel.imdb.rest.model.EditUserRequest;
import io.trane.future.CheckedFutureException;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author quannh22
 * @since 17/09/2019
 */
public interface SecurityService {
    DeferredResult<?> getUsers(IMDBClient client);

    DeferredResult<?> getUser(IMDBClient client, String username);

    DeferredResult<?> addUser(IMDBClient client, AddUserRequest addUserRequest);

    DeferredResult<?> editUser(IMDBClient client,String username, EditUserRequest editUserRequest) throws CheckedFutureException;

    DeferredResult<?> deleteUser(IMDBClient client, String username);

    DeferredResult<?> getRoles(IMDBClient client);

    DeferredResult<?> getRole(IMDBClient client, String username);

    DeferredResult<?> addRole(IMDBClient client, Role role);

    DeferredResult<?> editRole(IMDBClient client,String roleName, Role editRoleRequest);

    DeferredResult<?> deleteRole(IMDBClient client, String username);

    DeferredResult<?> getAuditLogs(IMDBClient client);
}
