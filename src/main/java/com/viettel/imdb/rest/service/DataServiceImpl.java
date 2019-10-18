package com.viettel.imdb.rest.service;


import com.facebook.presto.sql.parser.SqlParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.viettel.imdb.rest.util.StatisticClient;
import io.trane.future.Future;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;

import static com.viettel.imdb.rest.common.Utils.restResultToDeferredResult;
import static com.viettel.imdb.rest.common.Utils.throwableToHttpStatus;

/**
 * @author quannh22
 * @since 08/08/2019
 */
@Service
class DataServiceImpl implements DataService {
    SqlParser sqlParser = new SqlParser();

    private final IMDBClient client;
    // todo add StatisticClient here
    private final StatisticClient statisticClient;

    @Autowired
    public DataServiceImpl(IMDBClient client, StatisticClient statisticClient) {
        this.client = client;
        this.statisticClient = statisticClient;
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
        long previousTime = System.nanoTime();
        Future<Result> resultFuture = createTableFuture
                .map(aVoid -> {
                    statisticClient.addStatisticValueToRandomNode("vimdb_data_table_count", 1);
                    statisticClient.addLatencyInUsToRandomNode("write", (System.nanoTime() - previousTime) / 1000);
                    return new Result(HttpStatus.CREATED);
                })
                .rescue(throwable -> {
//                    statisticClient.addStatisticValueToRandomNode("vimdb_data_table_count", 1);
//                    statisticClient.addLatencyInUsToRandomNode("write", (System.nanoTime() - previousTime) / 1000);
                    return throwableToHttpStatus(throwable);
                });
        return restResultToDeferredResult(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> dropTable(String namespace, String tableName) {
        Logger.error("dropTable({}, {})", namespace, tableName);
        if(RestValidator.validateNamespace(namespace) != ErrorCode.NO_ERROR) {
            return Utils.INTERNAL_ERROR("namespace must be \"namespace\" by now!!!");
        }
        Future<Void> dropTableFuture = client.dropTable(tableName);
        long previousTime = System.nanoTime();
        Future<Result> resultFuture = dropTableFuture
                .map(aVoid -> {
                    statisticClient.addStatisticValueToRandomNode("vimdb_data_table_count", -1);
                    statisticClient.addLatencyInUsToRandomNode("write", (System.nanoTime() - previousTime) / 1000);
                    return new Result(HttpStatus.NO_CONTENT);
                })
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
        long previousTime = System.nanoTime();
        if(RestValidator.validateNamespace(namespace) != ErrorCode.NO_ERROR) {
            return Utils.INTERNAL_ERROR("namespace must be \"namespace\" by now!!!");
        }
        Future<Void> createIndexFuture = client.createIndex(tableName, indexName, indexType);
        Future<Result> resultFuture = createIndexFuture
                .map(aVoid -> {
//                    statisticClient.addStatisticValueToRandomNode("vimdb_data_table_count", 1);
                    statisticClient.addLatencyInUsToRandomNode("write", (System.nanoTime() - previousTime) / 1000);
                    return new Result(HttpStatus.CREATED);
                })
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
        long previousTime = System.nanoTime();
        Future<Result> resultFuture = dropIndexFuture
                .map(aVoid -> {
//                    statisticClient.addStatisticValueToRandomNode("vimdb_data_table_count", -1);
                    statisticClient.addLatencyInUsToRandomNode("write", (System.nanoTime() - previousTime) / 1000);
                    return new Result(HttpStatus.NO_CONTENT);
                })
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
        long previousTime = System.nanoTime();
        Future<Result> resultFuture = selectFuture
                .map(record -> {
                    statisticClient.addStatisticValueToRandomNode("vimdb_request_read_success", 1);
                    statisticClient.addLatencyInUsToRandomNode("read", (System.nanoTime() - previousTime) / 1000);
                    return new Result(HttpStatus.OK, record);
                })
                .rescue(throwable -> {
                    statisticClient.addStatisticValueToRandomNode("vimdb_request_read_failed", 1);
                    statisticClient.addLatencyInUsToRandomNode("read", (System.nanoTime() - previousTime) / 1000);
                    return throwableToHttpStatus(throwable);
                });
        return restResultToDeferredResult(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> insert(String namespace, String tableName, String key, List<Field> fieldList) {
        Logger.error("insert({}, {}, {}, {})", namespace, tableName, key, fieldList);
        if(RestValidator.validateNamespace(namespace) != ErrorCode.NO_ERROR) {
            return Utils.INTERNAL_ERROR("namespace must be \"namespace\" by now!!!");
        }
        Future<Void> insertFuture = client.insert(tableName, key, fieldList);
        long previousTime = System.nanoTime();
        Future<Result> resultFuture = insertFuture
                .map(aVoid -> {
                    statisticClient.addStatisticValueToRandomNode("vimdb_request_write_success", 1);
                    statisticClient.addLatencyInUsToRandomNode("write", (System.nanoTime() - previousTime) / 1000);
                    return new Result(HttpStatus.CREATED);
                })
                .rescue(throwable -> {
                    statisticClient.addStatisticValueToRandomNode("vimdb_request_write_failed", 1);
                    statisticClient.addLatencyInUsToRandomNode("write", (System.nanoTime() - previousTime) / 1000);
                    return throwableToHttpStatus(throwable);
                });
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
        long previousTime = System.nanoTime();
        Future<Result> resultFuture = updateFuture
                .map(aVoid -> {
                    statisticClient.addStatisticValueToRandomNode("vimdb_request_write_success", 1);
                    statisticClient.addLatencyInUsToRandomNode("write", (System.nanoTime() - previousTime) / 1000);
                    return new Result(HttpStatus.NO_CONTENT);
                })
                .rescue(throwable -> {
                    statisticClient.addStatisticValueToRandomNode("vimdb_request_write_failed", 1);
                    statisticClient.addLatencyInUsToRandomNode("write", (System.nanoTime() - previousTime) / 1000);
                    return throwableToHttpStatus(throwable);
                });
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
        long previousTime = System.nanoTime();
        Future<Result> resultFuture = deleteFuture
                .map(aVoid -> {
                    statisticClient.addStatisticValueToRandomNode("vimdb_request_delete_success", 1);
                    statisticClient.addLatencyInUsToRandomNode("delete", (System.nanoTime() - previousTime) / 1000);
                    return new Result(HttpStatus.NO_CONTENT);
                })
                .rescue(throwable -> {
                    statisticClient.addStatisticValueToRandomNode("vimdb_request_delete_failed", 1);
                    statisticClient.addLatencyInUsToRandomNode("delete", (System.nanoTime() - previousTime) / 1000);
                    return throwableToHttpStatus(throwable);
                });
        return restResultToDeferredResult(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> cmd() {
        DeferredResult<ResponseEntity<?>> res = new DeferredResult<>();
        ObjectMapper mapper = new ObjectMapper();

        String value = "{\n" +
                "  \"status\": 0,\n" +
                "  " +
                "\"data\": [\n" +
                "    {\n" +
                "      \"custId\": 1,\n" +
                "      \"custs\": [\n" +
                "        {\n" +
                "          \"name\": \"Record 01\",\n" +
                "          \"value\": \"tooooooooooooooooooooooooooo long value\"\n" +
                "        },\n" +
                "        {\n" +
                "      " +
                "    \"name\": \"Record 02\",\n" +
                "          \"value\": \"tooooooooooooooooooooooooooo long value\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

//        JsonNode node = mapper.convertValue(, JsonNode.class);
        res.setResult(new ResponseEntity<>(value, HttpStatus.OK));
        return res;
    }
}
