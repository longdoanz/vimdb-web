package com.viettel.imdb.rest.service;


import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.core.security.User;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.exception.ExceptionType;
import com.viettel.imdb.rest.mock.client.ClientSimulator;
import com.viettel.imdb.rest.model.AddUserRequest;
import com.viettel.imdb.rest.model.EditUserRequest;
import com.viettel.imdb.rest.model.UserInfo;
import io.trane.future.CheckedFutureException;
import io.trane.future.Future;
import org.pmw.tinylog.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.viettel.imdb.rest.common.Utils.restResultToDeferredResult2;
import static com.viettel.imdb.rest.common.Utils.throwableToHttpStatus;

/**
 * @author quannh22
 * @since 17/09/2019
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    //==========================================================
    // Main process function
    //==========================================================

    private static String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";

    public void validatePassword(String password) {
        if(password == null || password.length() < 6) {
            throw new ExceptionType.BadRequestError("PASSWORD_AT_LEAST_SIX_CHAR");
        }
        if (!password.matches(PASSWORD_REGEX))
            throw new ExceptionType.BadRequestError("PASSWORD_REQUIRE");
    }

    public void validateUserName(String username) {
        if(username == null || username.isEmpty())
            throw new ExceptionType.BadRequestError("USER_NAME_IS_REQUIRED");
        if (!username.matches("^[0-9A-Za-z_-]+$"))
            throw new ExceptionType.BadRequestError("USER_NAME_DONT_MATCH");
    }

    @Override
    public DeferredResult<?> getUsers(IMDBClient client) {
        // todo fake here
        Logger.info("getUsers()");

        DeferredResult<List<UserInfo>> returnValue = new DeferredResult<>();
        ((ClientSimulator) client).getUsers()
                .onSuccess(userList -> {
                            List<UserInfo> userInfoList = new ArrayList<>();
                            for (User user : userList) {
                                UserInfo userInfo = new UserInfo();
                                userInfo.setUsername(user.getUsername());
                                List<Role> roleList = getRoles(client, user.getRolenameList());
                                userInfo.setRoles(roleList);
                                userInfo.setAuthenticationMethod("RBAC");
                                userInfoList.add(userInfo);
                            }
                            returnValue.setResult(userInfoList);
                        }
                )
                .onFailure(returnValue::setErrorResult);

        return returnValue;
    }

    public List<Role> getRoles(IMDBClient client, List<String> roleNameList) {
        //Logger.info("getRoles({})", username);
        List<Role> roleList = new ArrayList<>();
        if(roleNameList == null)
            return roleList;
        for (String roleName : roleNameList) {
            try {
                client.readRole(roleName)
                        .onSuccess(roleList::add).get(Duration.ofMinutes(1));
            } catch (Exception e) {
                if(e instanceof ClientException)
                    throw new ExceptionType.VIMDBRestClientError(((ClientException)e).getErrorCode(),
                            "ERROR_ON_GET_ROLE");
                else
                    throw new ExceptionType.VIMDBRestClientError("ERROR_ON_GET_ROLE");
            }
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
    public DeferredResult<?> addUser(IMDBClient client, AddUserRequest addUserRequest) {
        Logger.info("addUser({})", addUserRequest);
        String username = addUserRequest.getUserName();
        validateUserName(username);
        String password = addUserRequest.getPassword();
        validatePassword(password);
        //add new role
        DeferredResult<?> returnValue = new DeferredResult<>();
        int roleCount = 0;
        int newRoleListLen = -1;
        if (addUserRequest.getNewRoles() != null) newRoleListLen = addUserRequest.getNewRoles().size();
        List<String> roleList = addUserRequest.getRoles();

        //add new role
        for (int i = 0; i < newRoleListLen; i++) {
            Role role = addUserRequest.getNewRoles().get(i);

            Logger.info(role.getRolename());

            try {
                client.createRole(role.getRolename(), role.getPrivilegeList()).get(Duration.ofMinutes(1));
            } catch (CheckedFutureException e) {
                Logger.error(e);
                returnValue.setErrorResult(new ExceptionType.VIMDBRestClientError(e.getCause().getMessage()));
                break;
            }
            roleCount++;
            roleList.add(role.getRolename());
        }
        //that bai, xoa role
        if (roleCount < newRoleListLen) {
            Logger.error("add role fail {}", roleCount);
            for (int i = 0; i < addUserRequest.getNewRoles().size(); i++) {
                client.deleteRole(addUserRequest.getNewRoles().get(i).getRolename());
            }
            return returnValue;
        }

        //add user
        client.createUser(username, password.getBytes(), roleList)
                .onSuccess(aVoid -> returnValue.setResult(null))
                .onFailure(returnValue::setErrorResult);

        return returnValue;
    }

    @Override
    public DeferredResult<?> editUser(IMDBClient client, String username, EditUserRequest editUserRequest) throws CheckedFutureException {
//        Logger.info("editUser({})", editUserRequest);
        DeferredResult<?> returnValue = new DeferredResult<>();
        //change password
        String password = editUserRequest.getPassword();
        if(password != null) {
            validatePassword(password);
            client.changePassword(username, editUserRequest.getPassword().getBytes()).get
                    (Duration.ofMinutes(1));
        }

        int roleCount = 0;
        int newRoleListLen = -1;
        if (editUserRequest.getNewRoles() != null) newRoleListLen = editUserRequest.getNewRoles().size();

        //add new role
        List<String> roleList = null;
        if(editUserRequest.getRoles() == null){
            try {
                User user = client.readUser(username).get(Duration.ofMinutes(1));
                roleList = user.getRolenameList();
            } catch (CheckedFutureException e) {
                e.printStackTrace();
            }
        }
        else roleList = editUserRequest.getRoles();
        for(int i = 0; i < newRoleListLen; i++){
            Role role = editUserRequest.getNewRoles().get(i);

            try {
                client.createRole(role.getRolename(), role.getPrivilegeList()).get(Duration.ofMinutes(1));
            } catch (Exception e) {
                Logger.error("Error on create role", e);
                if(e instanceof ClientException)
                    returnValue.setErrorResult(new ExceptionType.BadRequestError(((ClientException) e).getErrorCode(),
                            "ERROR_ON_CREATE_ROLE", new Object[] {role.getRolename()}));
                break;
            }
            roleCount++;
            roleList.add(role.getRolename());
        }
        //that bai, xoa role
        if (roleCount < newRoleListLen) {
            Logger.error("add role fail {}", roleCount);
            for (int i = 0; i < editUserRequest.getNewRoles().size(); i++) {
                client.deleteRole(editUserRequest.getNewRoles().get(i).getRolename());
            }
            return returnValue;
        }
        //edit role
        Future<Void> updateFuture = client.updateUser(username, roleList); // todo how about newRoles

        updateFuture
                .onSuccess(aVoid -> returnValue.setResult(null))
                .onFailure(returnValue::setErrorResult);
        return returnValue;
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
    public DeferredResult<?> getRoles(IMDBClient client) {
        // todo fake here
        Logger.info("getRoles()");

        DeferredResult<List<Role>> future = new DeferredResult<>();
        ((ClientSimulator) client).getRoles()
                .onSuccess(roles -> future.setResult(roles))
                .onFailure(future::setErrorResult);

        return future;
    }

    @Override
    public DeferredResult<?> getRole(IMDBClient client, String roleName) {
        Logger.info("getRole({})", roleName);
        Future<Role> getRoleFuture = client.readRole(roleName);
        Future<Result> resultFuture = getRoleFuture
                .map(user -> new Result(HttpStatus.OK, user))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<?> addRole(IMDBClient client, Role role) {
        Logger.info("addRoleRequest({})", role);
        Future<Void> addFuture = client.createRole(role.getRolename(), role.getPrivilegeList());
        Future<Result> resultFuture = addFuture
                .map(aVoid -> new Result(HttpStatus.CREATED))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<?> editRole(IMDBClient client, String roleName, Role role) {
        Logger.info("editRole({})", role);
        Future<Void> updateFuture = client.updateRole(roleName, role.getPrivilegeList()); // todo how about newRoles
        Future<Result> resultFuture = updateFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }

    @Override
    public DeferredResult<?> deleteRole(IMDBClient client, String roleName) {
        DeferredResult<?> future = new DeferredResult<>();
        client.deleteRole(roleName)
                .onSuccess(aVoid -> future.setResult(null))
                .onFailure(future::setErrorResult);
        return future;
    }

    @Override
    public DeferredResult<?> getAuditLogs(IMDBClient client) {
        // todo fake here
        Logger.info("getAuditLogs()");

        Future<List<String>> getFuture = ((ClientSimulator) client).getAuditLogs();
        Future<Result> resultFuture = getFuture
                .map(logs -> new Result(HttpStatus.OK, logs))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult2(resultFuture);
    }
}
