package com.viettel.imdb.rest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.model.FilterModel;
import io.trane.future.Future;

public interface DocumentService {

    Future<Result> scan(IMDBClient imdbClient, String db, String tableName, FilterModel filter, String fields);

    Future<Result> select(IMDBClient imdbClient, String db, String tableName, String key, String filter);

    Future<Result> insert(IMDBClient imdbClient, String db, String tableName, JsonNode jsonNode);

    Future<Result> update(IMDBClient imdbClient, String db, String tableName, String key, JsonNode jsonNode);

    Future<Result> delete(IMDBClient imdbClient, String db, String tableName, String key);
}
