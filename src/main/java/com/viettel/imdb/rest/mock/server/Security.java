package com.viettel.imdb.rest.mock.server;

import com.viettel.imdb.common.Record;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.core.security.User;
import io.trane.future.Future;

import java.util.ArrayList;
import java.util.List;

/**
 * @author quannh22
 * @since 15/10/2019
 */
public interface Security {
    /**
     * Get all users of the system
     * @return all users
     */
    Future<List<User>> getAllUsers();

    /**
     * Get all roels of the system
     * @return all roles
     */
    Future<List<Role>> getAllRoles();
    /**
     * Change user password
     *
     * @param username input username
     * @param newPassword new password
     * @return result wrapped by {@link Future}
     */
    Future<Void> changePassword(String username, byte[] newPassword);

    /**
     * Read user data with input username
     *
     * @param username  input username
     * @return the user data wrapped by {@link Future}
     */
    Future<User> readUser(String username);

    /**
     * Create a new user
     *
     * @param username username of user to be crated
     * @param password new user password
     * @param roleNameList initial roleName list
     * @return result of whether user is created successfully or exception, wrapped by {@link Future}
     */
    Future<Void> createUser(String username, byte[] password, List<String> roleNameList);
    default Future<Void> createUser(String username, byte[] password) {
        return createUser(username, password, new ArrayList<>());
    }

    /**
     * Update a user info
     *
     * @param username input username to be updated
     * @param roleNameList updated role name list
     * @return result of whether user is updated successfully or exception, wrapped by {@link Future}
     */
    Future<Void> updateUser(String username, List<String> roleNameList);

    /**
     * Delete a user
     *
     * @param username input username to be deleted
     * @return result of whether user is  deleted or exception, wrapped by {@link Future}
     */
    Future<Void> deleteUser(String username);


    /**
     * Read a role info
     *
     * @param rolename input role name
     * @return result of a Role, wrapped by {@link Future}
     */
    Future<Role> readRole(String rolename);

    /**
     * Create a new role
     *
     * @param roleName input role name
     * @param privilegeList input privilege list
     * @return result of whether role is created or exception, wrapped by {@link Future}
     */
    Future<Void> createRole(String roleName, List<String> privilegeList);
    default Future<Void> createRole(String roleName) {
        return createRole(roleName, new ArrayList<>());
    }

    /**
     * Update a role
     *
     * @param roleName input role name to be updated
     * @param privilegeList updated  privilege list
     * @return result of whether the role is updated or exception, wrapped by {@link Future}
     */
    Future<Void> updateRole(String roleName, List<String> privilegeList);

    /**
     * Delete a role
     *
     * @param roleName input role name to be deleted
     * @return result of whether the role is deleted or exception, wrapped by {@link Future}
     */
    Future<Void> deleteRole(String roleName);

    /**
     * Revoke all privileges of a role
     *
     * @param roleName input role name
     * @return result of whether the task is successfully or exception, wrapped by {@link Future}
     */
    Future<Void> revokeAllRole(String roleName);
    default Future<Void> grant(String roleName, String privilege, String resource) {
        return Future.VOID; // todo not support by now
    }
    default Future<Void> revoke(String roleName, String privilege, String resource) {
        return Future.VOID; // todo not support by now
    }
}
