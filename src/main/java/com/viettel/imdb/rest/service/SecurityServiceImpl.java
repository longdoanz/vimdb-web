package com.viettel.imdb.rest.service;


import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.core.security.User;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.mock.client.ClientSimulator;
import com.viettel.imdb.rest.model.*;
import io.trane.future.Future;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.viettel.imdb.rest.common.Utils.restResultToDeferredResult2;
import static com.viettel.imdb.rest.common.Utils.throwableToHttpStatus;

/**
 * @author quannh22
 * @since 17/09/2019
 */
@Service
public class SecurityServiceImpl implements SecurityService {
    private final IMDBClient client;

    @Autowired
    public SecurityServiceImpl(IMDBClient client) {
        this.client = client;
    }
    //==========================================================
    // Main process function
    //==========================================================

    @Override
    public DeferredResult<ResponseEntity<?>> getUsers(IMDBClient client) {
        // todo fake here
        Logger.error("getUsers()");
        Future<List<User>> getFuture = ((ClientSimulator)client).getUsers();

        Future<Result> resultFuture = getFuture
                .map(users -> new Result(HttpStatus.OK, users))
                .rescue(throwable -> throwableToHttpStatus(throwable));

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        resultFuture
                .onSuccess(result ->{
                    List<User> userList = (List<User>)result.getData();
                    List<UserInfo> userInfoList = new ArrayList<UserInfo>();
                    for (User user:userList){
                        UserInfo userInfo = new UserInfo();
                        userInfo.setUsername(user.getUsername());
                        List<Role> roleList = getRoles(client,user.getRolenameList());
                        userInfo.setRoles(roleList);
                        userInfo.setAuthenticationMethod("RBAC");
                        userInfoList.add(userInfo);
                    }
                    returnValue.setResult(new ResponseEntity<>(userInfoList, result.getHttpStatus()));
                    }
                )
                .onFailure(throwable -> {
                    throwable.printStackTrace();
                    Map<String, Object> body = new HashMap<>();
                    body.put("error", throwable.getMessage());
                    returnValue.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
                });

        return returnValue;
    }

    public List<Role> getRoles (IMDBClient client, List<String> roleNameList) {
        //Logger.error("getRoles({})", username);
        List<Role> roleList = new ArrayList<Role>();
        for (String roleName : roleNameList){
            Future<Role> getRoleFuture = client.readRole(roleName);
            Future<Result> resultFuture = getRoleFuture
                    .map(user -> new Result(HttpStatus.OK, user))
                    .rescue(throwable -> throwableToHttpStatus(throwable));
            resultFuture
                    .onSuccess(result ->
                            roleList.add((Role)result.getData())
                    )
                    .onFailure(throwable -> {
                        throwable.printStackTrace();
                    });
        }

        return roleList;
    }
    @Override
    public DeferredResult<ResponseEntity<?>> getUser(IMDBClient client, String username) {
        Logger.error("getUser({})", username);
        Future<User> getUserFuture = client.readUser(username);
        Future<Result> resultFuture = getUserFuture
                .map(user -> new Result(HttpStatus.OK, user))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        resultFuture
                .onSuccess(result ->{
                    User user = (User) result.getData();
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUsername(user.getUsername());
                    userInfo.setAuthenticationMethod("RBAC");
                    List<Role> roleList = getRoles(client, user.getRolenameList());
                    userInfo.setRoles(roleList);
                    returnValue.setResult(new ResponseEntity<>(userInfo, result.getHttpStatus()));
                }

                )
                .onFailure(throwable -> {
                    throwable.printStackTrace();
                    Map<String, Object> body = new HashMap<>();
                    body.put("error", throwable.getMessage());
                    returnValue.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
                });
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> addUser(IMDBClient client, AddUserRequest addUserRequest) {
        Logger.error("addUser({})", addUserRequest);
        Future<Void> addFuture = client.createUser(addUserRequest.getUserName(), addUserRequest.getPassword().getBytes(), addUserRequest.getRoles());
        Future<Result> resultFuture = addFuture
                .map(aVoid -> new Result(HttpStatus.CREATED))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> editUser(IMDBClient client, EditUserRequest editUserRequest) {
        Logger.error("editUser({})", editUserRequest);
        Future<Void> updateFuture = client.updateUser(editUserRequest.getUserName(), editUserRequest.getRoles()); // todo how about newRoles
        Future<Result> resultFuture = updateFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> deleteUser(IMDBClient client, String username) {
        Logger.error("deleteUser({})", username);
        Future<Void> deleteFuture = client.deleteUser(username);
        Future<Result> resultFuture = deleteFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .onFailure(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> getRoles(IMDBClient client) {
        // todo fake here
        Logger.error("getRoles()");

        Future<List<Role>> getFuture = ((ClientSimulator)client).getRoles();
        Future<Result> resultFuture = getFuture
                .map(roles -> new Result(HttpStatus.OK, roles))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> getRole(IMDBClient client, String roleName) {
        Logger.error("getRole({})", roleName);
        Future<Role> getRoleFuture = client.readRole(roleName);
        Future<Result> resultFuture = getRoleFuture
                .map(user -> new Result(HttpStatus.OK, user))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> addRole(IMDBClient client, AddRoleRequest addRoleRequest) {
        Logger.error("addRoleRequest({})", addRoleRequest);
        Future<Void> addFuture = client.createRole(addRoleRequest.getRoleName(), addRoleRequest.getPrivileges());
        Future<Result> resultFuture = addFuture
                .map(aVoid -> new Result(HttpStatus.CREATED))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> editRole(IMDBClient client, EditRoleRequest editRoleRequest) {
        Logger.error("editRole({})", editRoleRequest);
        Future<Void> updateFuture = client.updateRole(editRoleRequest.getRoleName(), editRoleRequest.getPrivileges()); // todo how about newRoles
        Future<Result> resultFuture = updateFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> deleteRole(IMDBClient client, String roleName) {
        Logger.error("deleteRole({})", roleName);
        Future<Void> deleteFuture = client.deleteRole(roleName);
        Future<Result> resultFuture = deleteFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .onFailure(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> getAuditLogs(IMDBClient client) {
        // todo fake here
        Logger.error("getAuditLogs()");

        Future<List<String>> getFuture = ((ClientSimulator)client).getAuditLogs();
        Future<Result> resultFuture = getFuture
                .map(logs -> new Result(HttpStatus.OK, logs))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }
}
