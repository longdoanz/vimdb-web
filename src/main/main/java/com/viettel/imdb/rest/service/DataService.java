package com.viettel.imdb.rest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.Field;
import com.viettel.imdb.rest.domain.RestIndexModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * @author quannh22
 * @since 08/08/2019
 */

public interface DataService {
    ResponseEntity<?> getDataInfo(IMDBClient client);

    DeferredResult<?> createNamespace(IMDBClient client, String namespace);

    DeferredResult<?> getTableListInNamespace(IMDBClient client, String namespace);

    DeferredResult<?> dropNamespace(IMDBClient client, String namespace);

    DeferredResult<?> updateNamespace(IMDBClient client, String namespace, String newname);

    DeferredResult<?> createTable(IMDBClient client, String namespace, String tableName);

    DeferredResult<?> dropTable(IMDBClient client, String namespace, String tableName);

    DeferredResult<?> createIndex(IMDBClient client, RestIndexModel indexModel);

    DeferredResult<?> dropIndex(IMDBClient client, String namespace, String tableName, String indexName);

    DeferredResult<?> select(IMDBClient client, String namespace, String tableName, String key, List<String> fieldNameList);

    DeferredResult<?> insert(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList);

    DeferredResult<?> insert(IMDBClient client, String namespace, String tableName, String key, String json);

    DeferredResult<?> update(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList);

    DeferredResult<?> update(IMDBClient client, String namespace, String tableName, String key, String json);

    DeferredResult<?> upsert(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList);

    DeferredResult<?> upsert(IMDBClient client, String namespace, String tableName, String key, String json);

    DeferredResult<?> replace(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList);

    DeferredResult<?> replace(IMDBClient client, String namespace, String tableName, String key, String json);

    DeferredResult<?> scan(IMDBClient client, String namespace, String tableName, String filter, List<String> fields);

    DeferredResult<?> delete(IMDBClient client, String namespace, String tableName, String key, List<String> fieldNameList);

    DeferredResult<?> cmd(IMDBClient client, JsonNode req);
}
