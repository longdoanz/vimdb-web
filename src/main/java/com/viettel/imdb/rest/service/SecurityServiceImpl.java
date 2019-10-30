package com.viettel.imdb.rest.service;


import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.core.security.User;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.exception.ExceptionType;
import com.viettel.imdb.rest.mock.client.ClientSimulator;
import com.viettel.imdb.rest.model.AddUserRequest;
import com.viettel.imdb.rest.model.EditRoleRequest;
import com.viettel.imdb.rest.model.EditUserRequest;
import com.viettel.imdb.rest.model.UserInfo;
import io.trane.future.CheckedFutureException;
import io.trane.future.Future;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.Duration;
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
        Logger.info("getUsers()");
        Future<List<User>> getFuture = ((ClientSimulator) client).getUsers();

        Future<Result> resultFuture = getFuture
                .map(users -> new Result(HttpStatus.OK, users))
                .rescue(throwable -> throwableToHttpStatus(throwable));

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        resultFuture
                .onSuccess(result -> {
                            List<User> userList = (List<User>) result.getData();
                            List<UserInfo> userInfoList = new ArrayList<UserInfo>();
                            for (User user : userList) {
                                UserInfo userInfo = new UserInfo();
                                userInfo.setUsername(user.getUsername());
                                List<Role> roleList = getRoles(client, user.getRolenameList());
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

    public List<Role> getRoles(IMDBClient client, List<String> roleNameList) {
        //Logger.info("getRoles({})", username);
        List<Role> roleList = new ArrayList<Role>();
        for (String roleName : roleNameList) {
            Future<Role> getRoleFuture = client.readRole(roleName);
            Future<Result> resultFuture = getRoleFuture
                    .map(user -> new Result(HttpStatus.OK, user))
                    .rescue(throwable -> throwableToHttpStatus(throwable));
            resultFuture
                    .onSuccess(result ->
                            roleList.add((Role) result.getData())
                    )
                    .onFailure(throwable -> {
                        throwable.printStackTrace();
                    });
        }

        return roleList;
    }

    @Override
    public DeferredResult<?> getUser(IMDBClient client, String username) {
        Logger.info("getUser({})", username);

        DeferredResult<UserInfo> returnValue = new DeferredResult<>();
        client.readUser(username)
                .onSuccess(user -> {
                            if (user == null) {
                                returnValue.setErrorResult(new ExceptionType.BadRequestError("User name not found"));
                                return;
                            }
                            UserInfo userInfo = new UserInfo();
                            userInfo.setUsername(user.getUsername());
                            userInfo.setAuthenticationMethod("RBAC");
                            List<Role> roleList = getRoles(client, user.getRolenameList());
                            userInfo.setRoles(roleList);
                            returnValue.setResult(userInfo);
                        }
                )
                .onFailure(returnValue::setErrorResult);

        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> addUser(IMDBClient client, AddUserRequest addUserRequest) {
        Logger.info("addUser({})", addUserRequest);
        //add new role
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        int roleCount = 0;
        int newRoleListLen = -1;
        if (addUserRequest.getNewRoles() != null) newRoleListLen = addUserRequest.getNewRoles().size();
        List<String> roleList = addUserRequest.getRoles();

        //add new role
        for (int i = 0; i < newRoleListLen; i++) {
            Role role = addUserRequest.getNewRoles().get(i);

            Logger.info(role.getRolename());
            Future<Void> addFuture = client.createRole(role.getRolename(), role.getPrivilegeList());
            Future<Result> resultFuture = addFuture
                    .map(aVoid -> new Result(HttpStatus.CREATED))
                    .rescue(throwable -> throwableToHttpStatus(throwable));
            Result result;
            try {
                result = resultFuture.get(Duration.ofMinutes(1));
            } catch (CheckedFutureException e) {
                e.printStackTrace();
                Map<String, Object> body = new HashMap<>();
                body.put("error", e.getMessage());
                returnValue.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
                break;
            }
            if (result.getHttpStatus() == HttpStatus.CREATED) {
                roleCount++;
                roleList.add(role.getRolename());
            } else {
                Map<String, Object> body = new HashMap<>();
                body.put("error", result.getMessage());
                returnValue.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
                break;
            }
        }
        //that bai, xoa role
        if (roleCount < newRoleListLen) {
            Logger.error("add role fail {}", roleCount);
            for (int i = 0; i < addUserRequest.getNewRoles().size(); i++) {
                Future<Void> addFuture = client.deleteRole(addUserRequest.getNewRoles().get(i).getRolename());
            }
            return returnValue;
        }
        //add user

        Future<Void> addFuture = client.createUser(addUserRequest.getUserName(), addUserRequest.getPassword().getBytes(), roleList);
        Future<Result> resultFuture = addFuture
                .map(aVoid -> new Result(HttpStatus.CREATED))
                .rescue(throwable -> throwableToHttpStatus(throwable));

        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> editUser(IMDBClient client, String username, EditUserRequest editUserRequest) {
        Logger.info("editUser({})", editUserRequest);

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        //change password
        Future<Void> changePasswordFuture = client.changePassword(username, editUserRequest.getPassword().getBytes());

        int roleCount = 0;
        int newRoleListLen = -1;
        if (editUserRequest.getNewRoles() != null) newRoleListLen = editUserRequest.getNewRoles().size();

        //add new role
        List<String> roleList = editUserRequest.getRoles();
        for (int i = 0; i < newRoleListLen; i++) {
            Role role = editUserRequest.getNewRoles().get(i);

            Logger.info(role.getRolename());
            Future<Void> addFuture = client.createRole(role.getRolename(), role.getPrivilegeList());
            Future<Result> resultFuture = addFuture
                    .map(aVoid -> new Result(HttpStatus.CREATED))
                    .rescue(throwable -> throwableToHttpStatus(throwable));
            Result result;
            try {
                result = resultFuture.get(Duration.ofMinutes(1));
            } catch (CheckedFutureException e) {
                e.printStackTrace();
                Map<String, Object> body = new HashMap<>();
                body.put("error", e.getMessage());
                returnValue.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
                break;
            }
            if (result.getHttpStatus() == HttpStatus.CREATED) {
                roleCount++;
                roleList.add(role.getRolename());
            } else {
                Map<String, Object> body = new HashMap<>();
                body.put("error", result.getMessage());
                returnValue.setResult(new ResponseEntity<>(body, result.getHttpStatus()));
                break;
            }
        }
        //that bai, xoa role
        if (roleCount < newRoleListLen) {
            Logger.error("add role fail {}", roleCount);
            for (int i = 0; i < editUserRequest.getNewRoles().size(); i++) {
                Future<Void> addFuture = client.deleteRole(editUserRequest.getNewRoles().get(i).getRolename());
            }
            return returnValue;
        }
        //edit role
        Future<Void> updateFuture = client.updateUser(username, roleList); // todo how about newRoles

        Future<Result> resultFuture = updateFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<?> deleteUser(IMDBClient client, String username) {
        Logger.info("deleteUser({})", username);

        DeferredResult<?> future = new DeferredResult<>();
        client.deleteUser(username)
                .onSuccess(aVoid -> future.setResult(null))
        .onFailure(future::setErrorResult);

        return future;
    }

    @Override
    public DeferredResult<List<Role>> getRoles(IMDBClient client) {
        // todo fake here
        Logger.info("getRoles()");
        DeferredResult<List<Role>> future = new DeferredResult<>();
        ((ClientSimulator) client).getRoles()
                .onSuccess(roles -> future.setResult(roles))
                .onFailure(future::setErrorResult);
        return future;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> getRole(IMDBClient client, String roleName) {
        Logger.info("getRole({})", roleName);
        Future<Role> getRoleFuture = client.readRole(roleName);
        Future<Result> resultFuture = getRoleFuture
                .map(user -> new Result(HttpStatus.OK, user))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> addRole(IMDBClient client, Role role) {
        Logger.info("addRoleRequest({})", role);
        Future<Void> addFuture = client.createRole(role.getRolename(), role.getPrivilegeList());
        Future<Result> resultFuture = addFuture
                .map(aVoid -> new Result(HttpStatus.CREATED))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> editRole(IMDBClient client, String roleName, Role role) {
        Logger.info("editRole({})", role);
        Future<Void> updateFuture = client.updateRole(roleName, role.getPrivilegeList()); // todo how about newRoles
        Future<Result> resultFuture = updateFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> deleteRole(IMDBClient client, String roleName) {
        Logger.info("deleteRole({})", roleName);
        Future<Void> deleteFuture = client.deleteRole(roleName);
        Future<Result> resultFuture = deleteFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .onFailure(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> getAuditLogs(IMDBClient client) {
        // todo fake here
        Logger.info("getAuditLogs()");

        Future<List<String>> getFuture = ((ClientSimulator) client).getAuditLogs();
        Future<Result> resultFuture = getFuture
                .map(logs -> new Result(HttpStatus.OK, logs))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }
}
