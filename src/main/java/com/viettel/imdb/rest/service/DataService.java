package com.viettel.imdb.rest.service;

import com.viettel.imdb.common.Field;
import com.viettel.imdb.rest.domain.RestIndexModel;
import com.viettel.imdb.rest.domain.RestScanModel;
import com.viettel.imdb.rest.model.NamespaceInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * @author quannh22
 * @since 08/08/2019
 */
public interface DataService {
    ResponseEntity<?> getDataInfo();

    DeferredResult<ResponseEntity<?>> createNamespace(String namespace);

    DeferredResult<ResponseEntity<?>> dropNamespace(String namespace);

    DeferredResult<ResponseEntity<?>> updateNamespace(String namespace, String newname);

    DeferredResult<ResponseEntity<?>> createTable(String namespace, String tableName);

    DeferredResult<ResponseEntity<?>> dropTable(String namespace, String tableName);

    DeferredResult<ResponseEntity<?>> createIndex(RestIndexModel indexModel);

    DeferredResult<ResponseEntity<?>> dropIndex(String namespace, String tableName, String indexName);

    DeferredResult<ResponseEntity<?>> select(String namespace, String tableName, String key, List<String> fieldNameList);

    DeferredResult<ResponseEntity<?>> insert(String namespace, String tableName, String key, List<Field> fieldList);

    DeferredResult<ResponseEntity<?>> insert(String namespace, String tableName, String key, String json);

    DeferredResult<ResponseEntity<?>> update(String namespace, String tableName, String key, List<Field> fieldList);

    DeferredResult<ResponseEntity<?>> update(String namespace, String tableName, String key, String json);

    DeferredResult<ResponseEntity<?>> upsert(String namespace, String tableName, String key, List<Field> fieldList);

    DeferredResult<ResponseEntity<?>> upsert(String namespace, String tableName, String key, String json);

    DeferredResult<ResponseEntity<?>> replace(String namespace, String tableName, String key, List<Field> fieldList);

    DeferredResult<ResponseEntity<?>> replace(String namespace, String tableName, String key, String json);

    DeferredResult<ResponseEntity<?>> scan(String namespace, String tableName, RestScanModel restScanModel);

    DeferredResult<ResponseEntity<?>> delete(String namespace, String tableName, String key, List<String> fieldNameList);
}
