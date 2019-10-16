package com.viettel.imdb.rest.service;


import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.Field;
import com.viettel.imdb.common.Record;
import com.viettel.imdb.common.ValueType;
import com.viettel.imdb.rest.common.RestValidator;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.common.Utils;
import com.viettel.imdb.rest.domain.RestIndexModel;
import com.viettel.imdb.rest.domain.RestScanModel;
import com.viettel.imdb.rest.mock.client.ClientSimulator;
import io.trane.future.Future;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

import static com.viettel.imdb.rest.common.Utils.restResultToDeferredResult;
import static com.viettel.imdb.rest.common.Utils.throwableToHttpStatus;

/**
 * @author quannh22
 * @since 08/08/2019
 */
@Service
class DataServiceImpl implements DataService {
    private final IMDBClient client;

    @Autowired
    public DataServiceImpl(IMDBClient client) {
        this.client = client;
    }

    //==========================================================
    // Main process function
    //==========================================================


    @Override
    public ResponseEntity<?> getDataInfo() {
//        List<NamespaceInformation> namespaces = (ClientSimulator)client
        return new ResponseEntity<>(((ClientSimulator)client).getNamespaces(), HttpStatus.OK);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> createNamespace(String namespace) {
        Logger.error("Create Namespace");

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> updateNamespace(String namespace, String newname) {
        Logger.error("Delete Namespace");

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> dropNamespace(String namespace) {
        Logger.error("Delete Namespace");

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> createTable(String namespace, String tableName) {
        Logger.error("Create table({}, {})", namespace, tableName);
        if(RestValidator.validateNamespace(namespace) != ErrorCode.NO_ERROR) {
            return Utils.INTERNAL_ERROR("namespace must be \"namespace\" by now!!!");
        }
        Future<Void> createTableFuture = client.createTable(tableName);
        Future<Result> resultFuture = createTableFuture
                .map(aVoid -> new Result(HttpStatus.CREATED))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> dropTable(String namespace, String tableName) {
        Logger.error("dropTable({}, {})", namespace, tableName);
        if(RestValidator.validateNamespace(namespace) != ErrorCode.NO_ERROR) {
            return Utils.INTERNAL_ERROR("namespace must be \"namespace\" by now!!!");
        }
        Future<Void> dropTableFuture = client.dropTable(tableName);
        Future<Result> resultFuture = dropTableFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> createIndex(RestIndexModel indexModel) {
        Logger.error("createIndex({})", indexModel);
        String namespace = indexModel.getNamespace();
        String tableName = indexModel.getTable();
        String indexName = indexModel.getName();
        ValueType indexType = indexModel.getType();
        if(RestValidator.validateNamespace(namespace) != ErrorCode.NO_ERROR) {
            return Utils.INTERNAL_ERROR("namespace must be \"namespace\" by now!!!");
        }
        Future<Void> createIndexFuture = client.createIndex(tableName, indexName, indexType);
        Future<Result> resultFuture = createIndexFuture
                .map(aVoid -> new Result(HttpStatus.CREATED))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult(resultFuture);

    }

    @Override
    public DeferredResult<ResponseEntity<?>> dropIndex(String namespace, String tableName, String indexName) {
        Logger.error("dropIndex({}, {}, {})", namespace, tableName, indexName);
        if(RestValidator.validateNamespace(namespace) != ErrorCode.NO_ERROR) {
            return Utils.INTERNAL_ERROR("namespace must be \"namespace\" by now!!!");
        }
        Future<Void> dropIndexFuture = client.dropIndex(tableName, indexName);
        Future<Result> resultFuture = dropIndexFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult(resultFuture);

    }

    @Override
    public DeferredResult<ResponseEntity<?>> select(String namespace, String tableName, String key, List<String> fieldNameList) {
        Logger.error("select({}, {}, {}, {})\n", namespace, tableName, key, fieldNameList);
        if(RestValidator.validateNamespace(namespace) != ErrorCode.NO_ERROR) {
            return Utils.INTERNAL_ERROR("namespace must be \"namespace\" by now!!!");
        }
        Future<Record> selectFuture = client.select(tableName, key, fieldNameList);
        Future<Result> resultFuture = selectFuture
                .map(record -> new Result(HttpStatus.OK, record))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> insert(String namespace, String tableName, String key, List<Field> fieldList) {
        Logger.error("insert({}, {}, {}, {})", namespace, tableName, key, fieldList);
        if(RestValidator.validateNamespace(namespace) != ErrorCode.NO_ERROR) {
            return Utils.INTERNAL_ERROR("namespace must be \"namespace\" by now!!!");
        }
        Future<Void> insertFuture = client.insert(tableName, key, fieldList);
        Future<Result> resultFuture = insertFuture
                .map(aVoid -> new Result(HttpStatus.CREATED))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> insert(String namespace, String tableName, String key, String json) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> update(String namespace, String tableName, String key, List<Field> fieldList) {
        Logger.error("update({}, {}, {}, {})", namespace, tableName, key, fieldList);
        if(RestValidator.validateNamespace(namespace) != ErrorCode.NO_ERROR) {
            return Utils.INTERNAL_ERROR("namespace must be \"namespace\" by now!!!");
        }
        Future<Void> updateFuture = client.update(tableName, key, fieldList);
        Future<Result> resultFuture = updateFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> update(String namespace, String tableName, String key, String json) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> upsert(String namespace, String tableName, String key, List<Field> fieldList) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> upsert(String namespace, String tableName, String key, String json) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> replace(String namespace, String tableName, String key, List<Field> fieldList) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> replace(String namespace, String tableName, String key, String json) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> scan(String namespace, String tableName, RestScanModel restScanModel) {
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> delete(String namespace, String tableName, String key, List<String> fieldNameList) {
        Logger.error("delete({}, {}, {}, {})", namespace, tableName, key, fieldNameList);
        if(RestValidator.validateNamespace(namespace) != ErrorCode.NO_ERROR) {
            return Utils.INTERNAL_ERROR("namespace must be \"namespace\" by now!!!");
        }
        Future<Void> deleteFuture = client.delete(tableName, key, fieldNameList);
        Future<Result> resultFuture = deleteFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .onFailure(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult(resultFuture);
    }
}
