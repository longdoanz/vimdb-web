package com.viettel.imdb.rest.service;


//import com.facebook.presto.sql.parser.SqlParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.Field;
import com.viettel.imdb.common.Record;
import com.viettel.imdb.common.ValueType;
import com.viettel.imdb.rest.common.RestValidator;
import com.viettel.imdb.rest.common.Result;
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

import java.io.IOException;
import java.util.List;

import static com.viettel.imdb.rest.common.Utils.restResultToDeferredResult;
import static com.viettel.imdb.rest.common.Utils.throwableToHttpStatus;

/**
 * @author quannh22
 * @since 08/08/2019
 */
@Service
class DataServiceImpl implements DataService {
//    SqlParser sqlParser = new SqlParser();

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
    public ResponseEntity<?> getDataInfo(IMDBClient client) {
//        List<NamespaceInformation> namespaces = (ClientSimulator)client
        return new ResponseEntity<>(((ClientSimulator)client).getNamespaces(), HttpStatus.OK);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> createNamespace(IMDBClient client, String namespace) {
        Logger.info("Create Namespace");

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> getTableListInNamespace(IMDBClient client, String namespace) {
        RestValidator.validateNamespace(namespace);

        DeferredResult<ResponseEntity<?>> res = new DeferredResult<>();
        res.setResult(new ResponseEntity<>(((ClientSimulator)client).getTableList(namespace), HttpStatus.OK));
        return res;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> updateNamespace(IMDBClient client, String namespace, String newname) {
        Logger.info("Delete Namespace");

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> dropNamespace(IMDBClient client, String namespace) {
        Logger.info("Delete Namespace");

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> createTable(IMDBClient client, String namespace, String tableName) {
        Logger.info("Create table({}, {})", namespace, tableName);
        RestValidator.validateNamespace(namespace);

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
    public DeferredResult<ResponseEntity<?>> dropTable(IMDBClient client, String namespace, String tableName) {
        Logger.info("dropTable({}, {})", namespace, tableName);
        RestValidator.validateNamespace(namespace);

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
    public DeferredResult<ResponseEntity<?>> createIndex(IMDBClient client, RestIndexModel indexModel) {
        Logger.info("createIndex({})", indexModel);
        String namespace = indexModel.getNamespace();
        String tableName = indexModel.getTable();
        String indexName = indexModel.getName();
        ValueType indexType = indexModel.getType();
        long previousTime = System.nanoTime();
        RestValidator.validateNamespace(namespace);

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
    public DeferredResult<ResponseEntity<?>> dropIndex(IMDBClient client, String namespace, String tableName, String indexName) {
        Logger.info("dropIndex({}, {}, {})", namespace, tableName, indexName);
        RestValidator.validateNamespace(namespace);

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
    public DeferredResult<ResponseEntity<?>> select(IMDBClient client, String namespace, String tableName, String key, List<String> fieldNameList) {
        Logger.info("select({}, {}, {}, {})\n", namespace, tableName, key, fieldNameList);
        RestValidator.validateNamespace(namespace);

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
    public DeferredResult<ResponseEntity<?>> insert(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList) {
        Logger.info("insert({}, {}, {}, {})", namespace, tableName, key, fieldList);
        RestValidator.validateNamespace(namespace);

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
    public DeferredResult<ResponseEntity<?>> insert(IMDBClient client, String namespace, String tableName, String key, String json) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> update(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList) {
        Logger.info("update({}, {}, {}, {})", namespace, tableName, key, fieldList);
        RestValidator.validateNamespace(namespace);

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
    public DeferredResult<ResponseEntity<?>> update(IMDBClient client, String namespace, String tableName, String key, String json) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> upsert(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> upsert(IMDBClient client, String namespace, String tableName, String key, String json) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> replace(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> replace(IMDBClient client, String namespace, String tableName, String key, String json) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> scan(IMDBClient client, String namespace, String tableName, RestScanModel restScanModel) {
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> delete(IMDBClient client, String namespace, String tableName, String key, List<String> fieldNameList) {
        Logger.info("delete({}, {}, {}, {})", namespace, tableName, key, fieldNameList);
        RestValidator.validateNamespace(namespace);

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
    public DeferredResult<ResponseEntity<?>> cmd(IMDBClient client, JsonNode req) {
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

        JsonNode node = null;
        try {
            node = mapper.readTree(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        res.setResult(new ResponseEntity<>(node, HttpStatus.OK));
        return res;
    }
}
