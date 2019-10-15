package com.viettel.imdb.rest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.model.FilterModel;
import io.trane.future.Future;

public interface DocumentService {

    Future<Result> scan(String db, String tableName, FilterModel filter, String fields);

    Future<Result> select(String db, String tableName, String key, String filter);

    Future<Result> insert(String db, String tableName, JsonNode jsonNode);

    Future<Result> update(String db, String tableName, String key, JsonNode jsonNode);

    Future<Result> delete(String db, String tableName, String key);
}
