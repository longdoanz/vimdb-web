package com.viettel.imdb.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.common.Field;
import com.viettel.imdb.rest.RestErrorCode;
import com.viettel.imdb.rest.common.PasswordUtil;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.common.Utils;
import com.viettel.imdb.rest.model.Privilege;
import com.viettel.imdb.rest.model.PrivilegeType;
import com.viettel.imdb.rest.model.User;
import io.trane.future.Future;
import io.trane.future.Promise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserServiceImpl implements UserService {

    private static final String TABLE_NAME = "internal_rest_user";
    private static final String ALL_TABLE_SIGN = "*";
    private static final String DEFAULT_DATABASE = "default";

    @Autowired
    private IMDBClient imdbClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        try {
//            imdbClient.createTable(TABLE_NAME).get(Duration.ofDays(1));
        } catch (ClientException e) {
            if(ErrorCode.TABLE_EXIST.equals(e.getErrorCode())) {
                return;
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Future<Result> insertUser(JsonNode jsonUser) {
        Promise<Result> future = Promise.apply();
        Result result = new Result();

        Result validateResult = validate(jsonUser);
        if(!validateResult.isSuccess()) {
            result.setHttpStatus(HttpStatus.BAD_REQUEST);
            result.setMessage(validateResult.getMessage());
            future.setValue(result);
            return future;
        }

        User user = (User)validateResult.getData();
        String hashPass = passwordEncoder.encode(user.getPassword());
        ((ObjectNode)jsonUser).put("password", hashPass);

        imdbClient.upsert(TABLE_NAME, user.getUsername(), Utils.getFieldValue(jsonUser)).onSuccess(aVoid -> {
            result.setHttpStatus(HttpStatus.CREATED);
            future.setValue(result);
        }).onFailure(throwable -> {
            ErrorCode errorCode = ((ClientException) throwable).getErrorCode();
            if(ErrorCode.KEY_EXIST.equals(errorCode)) {
                result.setHttpStatus(HttpStatus.BAD_REQUEST);
                result.setMessage(RestErrorCode.USERNAME_EXIST.name());
            } else {
                result.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setMessage(errorCode.name());
            }
            future.setValue(result);
        });

        return future;
    }

    public Future<Result> changePassword(String username, String newPassword) {
        Promise<Result> future = Promise.apply();
        Result result = new Result();

        PasswordUtil.Result passwordValidateResult = PasswordUtil.validatePassword(username, newPassword);
        if(!passwordValidateResult.isAccepted()) {
            result.setMessage(passwordValidateResult.getError());
            result.setHttpStatus(HttpStatus.BAD_REQUEST);
            future.setValue(result);
            return future;
        }

        imdbClient.update(TABLE_NAME, username, new ArrayList<Field>(){{
            String hashPass = passwordEncoder.encode(newPassword);
            String passValue = "\"" + hashPass + "\"";
            add(new Field("password", passValue.getBytes()));
        }}).onSuccess(aVoid -> {
            result.setHttpStatus(HttpStatus.OK);
            future.setValue(result);
        }).onFailure(throwable -> {
            ErrorCode errorCode = ((ClientException) throwable).getErrorCode();
            if(ErrorCode.KEY_NOT_EXIST.equals(errorCode)) {
                result.setHttpStatus(HttpStatus.BAD_REQUEST);
                result.setMessage(RestErrorCode.USER_NOT_EXIST.name());
            } else {
                result.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setMessage(errorCode.name());
            }
            future.setValue(result);
        });

        return future;
    }

    private Result validate(JsonNode jsonUser) {
        ObjectMapper mapper = new ObjectMapper();

        User user = null;
        try {
            user = mapper.treeToValue(jsonUser, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            if(e.getMessage().contains("No enum constant")) {
                return new Result(false, RestErrorCode.PRIVILEGE_TYPE_NOT_EXIST.name());
            }
            return new Result(false, RestErrorCode.WRONG_JSON_FORMAT.name());
        }

        PasswordUtil.Result userValidateResult = PasswordUtil.validateUsername(user.getUsername());
        if(!userValidateResult.isAccepted()) {
            return new Result(false, userValidateResult.getError());
        }
        PasswordUtil.Result passwordValidateResult = PasswordUtil.validatePassword(user.getUsername(), user.getPassword());
        if(!passwordValidateResult.isAccepted()) {
            return new Result(false, passwordValidateResult.getError());
        }

        Set<String> tablenames = new HashSet<>();
        for(Privilege privilege : user.getPrivileges()) {
            if(PrivilegeType.READ_WRITE.equals(privilege.getType()) || PrivilegeType.READ.equals(privilege.getType())) {
                if(privilege.getDbName()==null || privilege.getDbName().isEmpty() || !DEFAULT_DATABASE.equals(privilege.getDbName())) {
                    return new Result(false, RestErrorCode.DATABASE_NOT_EXIST.name());
                }
                if(privilege.getTables()==null || privilege.getTables().isEmpty()) {
                    return new Result(false, RestErrorCode.MISSING_INFORMATION.name());
                } else {
                    tablenames.addAll(privilege.getTables());
                }
            }
        }

        Result result = new Result(true, user);
        tablenames.parallelStream().forEach(s -> {
            try {
                if(!ALL_TABLE_SIGN.equals(s)) {
                    imdbClient.select(s, "a").get(Duration.ofDays(1));
                }
            } catch (Exception e) {
                if(ErrorCode.TABLE_NOT_EXIST.equals(((ClientException)e).getErrorCode())) {
                    result.setSuccess(false);
                    result.setMessage(ErrorCode.TABLE_NOT_EXIST.name());
                }
            }
        });

        if(!result.isSuccess()) {
            return result;
        }

        if(user.getParentRoles()==null || user.getParentRoles().isEmpty()) {
            return result;
        }

        user.getParentRoles().parallelStream().forEach(s -> {
            try {
                imdbClient.select(TABLE_NAME, s).get(Duration.ofDays(1));
            } catch (Exception e) {
                if(ErrorCode.KEY_NOT_EXIST.equals(((ClientException)e).getErrorCode())) {
                    result.setSuccess(false);
                    result.setMessage(RestErrorCode.PARENT_ROLE_NOT_EXIST.name());
                }
            }
        });

        if(!result.isSuccess()) {
            return result;
        }

        return result;
    }

    public Future<Result> deleteUser(String username) {
        Promise<Result> future = Promise.apply();
        Result result = new Result();

        imdbClient.delete(TABLE_NAME, username).onSuccess(aVoid -> {
            result.setHttpStatus(HttpStatus.NO_CONTENT);
            future.setValue(result);
        }).onFailure(throwable -> {
            ErrorCode errorCode = ((ClientException) throwable).getErrorCode();
            if(ErrorCode.KEY_NOT_EXIST.equals(errorCode)) {
                result.setHttpStatus(HttpStatus.BAD_REQUEST);
                result.setMessage(RestErrorCode.USER_NOT_EXIST.name());
            } else {
                result.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setMessage(errorCode.name());
            }
            future.setValue(result);
        });

        return future;
    }

    public Future<Result> getUser(String username) {
        Promise<Result> future = Promise.apply();
        Result result = new Result();
        ObjectMapper mapper = new ObjectMapper();

        imdbClient.select(TABLE_NAME, username).onSuccess(record -> {
            User user = null;
            try {
                JsonNode jsonUser = Utils.convertImdbFieldsToJsonNode(record.getValue());
                user = mapper.treeToValue(jsonUser, User.class);
                for (String parentRole : user.getParentRoles()) {
                    try {
                        record = imdbClient.select(TABLE_NAME, parentRole).get(Duration.ofDays(1));
                        jsonUser = Utils.convertImdbFieldsToJsonNode(record.getValue());
                        User parent = mapper.treeToValue(jsonUser, User.class);
                        user.getPrivileges().addAll(parent.getPrivileges());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(user!=null) {
                result.setHttpStatus(HttpStatus.OK);
                result.setData(user);
            } else {
                result.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            future.setValue(result);
        }).onFailure(throwable -> {
            throwable.printStackTrace();
            ErrorCode errorCode = ((ClientException) throwable).getErrorCode();
            if(ErrorCode.KEY_NOT_EXIST.equals(errorCode)) {
                result.setHttpStatus(HttpStatus.BAD_REQUEST);
                result.setMessage(RestErrorCode.USER_NOT_EXIST.name());
            } else {
                result.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setMessage(errorCode.name());
            }
            future.setValue(result);
        });

        return future;
    }

    public boolean checkAccessRight(PrivilegeType privilegeType, String dbName, String tableName) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();

        switch (privilegeType) {
            case READ: {
                if(checkAccessRightCaseRead(dbName, tableName, user.getPrivileges()))
                    return true;
                break;
            }
            case READ_WRITE: {
                if(checkAccessRightCaseReadWrite(dbName, tableName, user.getPrivileges()))
                    return true;
                break;
            }
            case DATA_ADMIN:
            case SYS_ADMIN:
            case USER_ADMIN: {
                for(Privilege privilege : user.getPrivileges()) {
                    if(PrivilegeType.SUPER_ADMIN.equals(privilege.getType()) || privilegeType.equals(privilege.getType())) {
                        return true;
                    }
                }
            }
            default: {
                break;
            }
        }
        return false;
    }

    private boolean checkAccessRightCaseRead(String dbName, String tableName, List<Privilege> privileges) {
        for(Privilege privilege : privileges) {
            switch (privilege.getType()) {
                case READ:
                case READ_WRITE: {
                    if(dbName.equals(privilege.getDbName())) {
                        if(privilege.getTables().contains(ALL_TABLE_SIGN) || privilege.getTables().contains(tableName)) {
                            return true;
                        }
                    }
                    break;
                }
                case DATA_ADMIN:
                case SUPER_ADMIN: {
                    return true;
                }
                default: {
                    break;
                }
            }
        }
        return false;
    }

    private boolean checkAccessRightCaseReadWrite(String dbName, String tableName, List<Privilege> privileges) {
        for(Privilege privilege : privileges) {
            switch (privilege.getType()) {
                case READ_WRITE: {
                    if(dbName.equals(privilege.getDbName())) {
                        if(privilege.getTables().contains(ALL_TABLE_SIGN) || privilege.getTables().contains(tableName)) {
                            return true;
                        }
                    }
                    break;
                }
                case DATA_ADMIN:
                case SUPER_ADMIN: {
                    return true;
                }
                default: {
                    break;
                }
            }
        }
        return false;
    }

}
