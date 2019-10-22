package com.viettel.imdb.rest.service;

import com.viettel.imdb.IMDBClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.viettel.imdb.common.Field;
import com.viettel.imdb.rest.domain.RestIndexModel;
import com.viettel.imdb.rest.domain.RestScanModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * @author quannh22
 * @since 08/08/2019
 */

public interface DataService {
    ResponseEntity<?> getDataInfo(IMDBClient client);

    DeferredResult<ResponseEntity<?>> createNamespace(IMDBClient client, String namespace);

    DeferredResult<ResponseEntity<?>> getTableListInNamespace(IMDBClient client, String namespace);

    DeferredResult<ResponseEntity<?>> dropNamespace(IMDBClient client, String namespace);

    DeferredResult<ResponseEntity<?>> updateNamespace(IMDBClient client, String namespace, String newname);

    DeferredResult<ResponseEntity<?>> createTable(IMDBClient client, String namespace, String tableName);

    DeferredResult<ResponseEntity<?>> dropTable(IMDBClient client, String namespace, String tableName);

    DeferredResult<ResponseEntity<?>> createIndex(IMDBClient client, RestIndexModel indexModel);

    DeferredResult<ResponseEntity<?>> dropIndex(IMDBClient client, String namespace, String tableName, String indexName);

    DeferredResult<ResponseEntity<?>> select(IMDBClient client, String namespace, String tableName, String key, List<String> fieldNameList);

    DeferredResult<ResponseEntity<?>> insert(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList);

    DeferredResult<ResponseEntity<?>> insert(IMDBClient client, String namespace, String tableName, String key, String json);

    DeferredResult<ResponseEntity<?>> update(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList);

    DeferredResult<ResponseEntity<?>> update(IMDBClient client, String namespace, String tableName, String key, String json);

    DeferredResult<ResponseEntity<?>> upsert(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList);

    DeferredResult<ResponseEntity<?>> upsert(IMDBClient client, String namespace, String tableName, String key, String json);

    DeferredResult<ResponseEntity<?>> replace(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList);

    DeferredResult<ResponseEntity<?>> replace(IMDBClient client, String namespace, String tableName, String key, String json);

    DeferredResult<ResponseEntity<?>> scan(IMDBClient client, String namespace, String tableName, RestScanModel restScanModel);

    DeferredResult<ResponseEntity<?>> delete(IMDBClient client, String namespace, String tableName, String key, List<String> fieldNameList);

    DeferredResult<ResponseEntity<?>> cmd(IMDBClient client, JsonNode req);
}
