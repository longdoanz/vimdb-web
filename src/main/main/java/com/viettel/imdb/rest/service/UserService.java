package com.viettel.imdb.rest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.model.PrivilegeType;
import io.trane.future.Future;

public interface UserService {

    Future<Result> insertUser(JsonNode jsonUser);

    Future<Result> changePassword(String username, String newPassword);

    Future<Result> getUser(String username);

    boolean checkAccessRight(PrivilegeType privilegeType, String dbName, String tableName);

    Future<Result> deleteUser(String username);

}
