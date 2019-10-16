package com.viettel.imdb.rest.mock.server;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.core.security.User;
import io.trane.future.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author quannh22
 * @since 15/10/2019
 */
public class SecurityImpl implements Security {
    private static SecurityImpl instance;
    private Map<String, User> userMap;
    private Map<String, Role> roleMap;

    public static Security getInstance() {
        if(instance == null) {
            instance = new SecurityImpl();
        }
        return instance;
    }

    private SecurityImpl() {
        userMap = new ConcurrentHashMap<>();
        roleMap = new ConcurrentHashMap<>();
        userMap.put("admin", new User("admin", "admin", new ArrayList<String>() {{
            add("admin");
        }}));
        roleMap.put("admin", new Role("admin", new ArrayList<String>() {{
            add("*");
        }}));
    }

    @Override
    public Future<List<User>> getAllUsers() {
        return Future.value(new ArrayList<>(userMap.values()));
    }

    @Override
    public Future<List<Role>> getAllRoles() {
        return Future.value(new ArrayList<>(roleMap.values()));
    }

    @Override
    public Future<Void> changePassword(String username, byte[] newPassword) {
        // todo validate
        if(!userMap.containsKey(username))
            return Future.exception(new ClientException(ErrorCode.KEY_NOT_EXIST));
        User user = userMap.get(username);
        user.setPassword(new String(newPassword));
        return Future.VOID;
    }

    @Override
    public Future<User> readUser(String username) {
        // todo validate
        if(!userMap.containsKey(username))
            return Future.exception(new ClientException(ErrorCode.KEY_NOT_EXIST));
        return Future.value(userMap.get(username));
    }

    @Override
    public Future<Void> createUser(String username, byte[] password, List<String> roleNameList) {
        if(userMap.containsKey(username))
            return Future.exception(new ClientException(ErrorCode.KEY_EXIST));
        for(String roleName : roleNameList)
            if(!roleMap.containsKey(roleName))
                return Future.exception(new ClientException(ErrorCode.KEY_NOT_EXIST));
        userMap.put(username, new User(username, new String(password), roleNameList)); // todo multithread here
        return Future.VOID;
    }

    @Override
    public Future<Void> updateUser(String username, List<String> roleNameList) {
        if(!userMap.containsKey(username))
            return Future.exception(new ClientException(ErrorCode.KEY_NOT_EXIST));
        for(String roleName : roleNameList)
            if(!roleMap.containsKey(roleName))
                return Future.exception(new ClientException(ErrorCode.KEY_NOT_EXIST));
        User user = userMap.get(username);
        user.setRolenameList(roleNameList);
        return Future.VOID;
    }

    @Override
    public Future<Void> deleteUser(String username) {
        if(userMap.containsKey(username))
            return Future.exception(new ClientException(ErrorCode.KEY_EXIST));
        userMap.remove(username);
        return Future.VOID;
    }

    @Override
    public Future<Role> readRole(String rolename) {
        if(!roleMap.containsKey(rolename))
            return Future.exception(new ClientException(ErrorCode.KEY_NOT_EXIST));
        return Future.value(roleMap.get(rolename));
    }

    @Override
    public Future<Void> createRole(String roleName, List<String> privilegeList) {
        if(roleMap.containsKey(roleName))
            return Future.exception(new ClientException(ErrorCode.KEY_EXIST));
        // todo validate privileges
        roleMap.put(roleName, new Role(roleName, privilegeList)); // todo multithread here
        return Future.VOID;
    }

    @Override
    public Future<Void> updateRole(String roleName, List<String> privilegeList) {
        if(!roleMap.containsKey(roleName))
            return Future.exception(new ClientException(ErrorCode.KEY_NOT_EXIST));
        // todo validate privileges
        Role role = roleMap.get(roleName);
        role.setPrivilegeList(privilegeList);
        return Future.VOID;
    }

    @Override
    public Future<Void> deleteRole(String roleName) {
        if(!roleMap.containsKey(roleName))
            return Future.exception(new ClientException(ErrorCode.KEY_NOT_EXIST));
        roleMap.remove(roleName);
        return Future.VOID;
    }

    @Override
    public Future<Void> revokeAllRole(String roleName) {
        if(!roleMap.containsKey(roleName))
            return Future.exception(new ClientException(ErrorCode.KEY_NOT_EXIST));
        Role role = roleMap.get(roleName);
        role.setPrivilegeList(new ArrayList<>()); // todo multithread
        return Future.VOID;
    }
}
