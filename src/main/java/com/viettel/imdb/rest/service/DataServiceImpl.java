package com.viettel.imdb.rest.service;


//import com.facebook.presto.sql.parser.SqlParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.Field;
import com.viettel.imdb.common.KeyRecord;
import com.viettel.imdb.common.Record;
import com.viettel.imdb.common.ValueType;
import com.viettel.imdb.rest.common.RestValidator;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.domain.RestIndexModel;
import com.viettel.imdb.rest.exception.ExceptionType;
import com.viettel.imdb.rest.mock.client.ClientSimulator;
import com.viettel.imdb.rest.model.FilterModel;
import com.viettel.imdb.rest.util.StatisticClient;
import io.trane.future.CheckedFutureException;
import io.trane.future.Future;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
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
    private static String TABLE_AND_FIELD_PATTERN = "[a-zA-Z_]+\\w*";
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


    private void validateTableNameName(String name) {
        if(name == null)
            throw new ExceptionType.BadRequestError("TABLE_NAME_IS_REQUIRED");
        if (!name.matches(TABLE_AND_FIELD_PATTERN)) {
            throw new ExceptionType.BadRequestError("TABLE_NAME_INVALID", name);
        }
    }

    @Override
    public ResponseEntity<?> getDataInfo(IMDBClient client) {
//        List<NamespaceInformation> namespaces = (ClientSimulator)client
        return new ResponseEntity<>(((ClientSimulator)client).getNamespaces(), HttpStatus.OK);
    }

    @Override
    public DeferredResult<?> createNamespace(IMDBClient client, String namespace) {
        Logger.info("Create Namespace");

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
        return returnValue;
    }

    @Override
    public DeferredResult<?> getTableListInNamespace(IMDBClient client, String namespace) {
        RestValidator.validateNamespace(namespace);

        DeferredResult<ResponseEntity<?>> res = new DeferredResult<>();
        res.setResult(new ResponseEntity<>(((ClientSimulator)client).getTableList(namespace), HttpStatus.OK));
        return res;
    }

    @Override
    public DeferredResult<?> updateNamespace(IMDBClient client, String namespace, String newname) {
        throw new ExceptionType.NotImplementError();
        /*DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
        return returnValue;*/
    }

    @Override
    public DeferredResult<?> dropNamespace(IMDBClient client, String namespace) {
        throw new ExceptionType.NotImplementError();
//        DeferredResult<?> returnValue = new DeferredResult<>();
//        return returnValue;
    }

    @Override
    public DeferredResult<?> createTable(IMDBClient client, String namespace, String tableName) {
        Logger.info("Create table({}, {})", namespace, tableName);
        RestValidator.validateNamespace(namespace);
        validateTableNameName(tableName);

        DeferredResult<?> returnValue = new DeferredResult<>();

        Future<Void> createTableFuture = client.createTable(tableName);
        long previousTime = System.nanoTime();
        createTableFuture
                .onSuccess(aVoid -> {
                    statisticClient.addStatisticValueToRandomNode("vimdb_data_table_count", 1);
                    statisticClient.addLatencyInUsToRandomNode("write", (System.nanoTime() - previousTime) / 1000);
                    returnValue.setResult(null);
                })
                .onFailure(throwable -> {
                    returnValue.setErrorResult(throwable);
//                    statisticClient.addStatisticValueToRandomNode("vimdb_data_table_count", 1);
//                    statisticClient.addLatencyInUsToRandomNode("write", (System.nanoTime() - previousTime) / 1000);
//                    return throwableToHttpStatus(throwable);
                });
        return returnValue;
//        return restResultToDeferredResult(resultFuture);
    }

    @Override
    public DeferredResult<?> dropTable(IMDBClient client, String namespace, String tableName) {
        Logger.info("dropTable({}, {})", namespace, tableName);
        RestValidator.validateNamespace(namespace);
        validateTableNameName(tableName);

        DeferredResult<?> returnValue = new DeferredResult<>();

        long previousTime = System.nanoTime();
        Future<Void> res = client.dropTable(tableName)
                .onSuccess(aVoid -> {
                    statisticClient.addStatisticValueToRandomNode("vimdb_data_table_count", -1);
                    statisticClient.addLatencyInUsToRandomNode("write", (System.nanoTime() - previousTime) / 1000);
                    returnValue.setResult(null);
                })
                .onFailure(returnValue::setErrorResult);

        return returnValue;
    }

    @Override
    public DeferredResult<?> createIndex(IMDBClient client, RestIndexModel indexModel) {
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
    public DeferredResult<?> dropIndex(IMDBClient client, String namespace, String tableName, String indexName) {
        Logger.info("dropIndex({}, {}, {})", namespace, tableName, indexName);
        RestValidator.validateNamespace(namespace);
        validateTableNameName(tableName);

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
    public DeferredResult<?> select(IMDBClient client, String namespace, String tableName, String key, List<String> fieldNameList) {
        Logger.info("select({}, {}, {}, {})\n", namespace, tableName, key, fieldNameList);
        RestValidator.validateNamespace(namespace);
        validateTableNameName(tableName);

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
    public DeferredResult<?> insert(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList) {
        Logger.info("insert({}, {}, {}, {})", namespace, tableName, key, fieldList);
        RestValidator.validateNamespace(namespace);
        validateTableNameName(tableName);

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
    public DeferredResult<?> insert(IMDBClient client, String namespace, String tableName, String key, String json) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<?> update(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList) {
        Logger.info("update({}, {}, {}, {})", namespace, tableName, key, fieldList);
        RestValidator.validateNamespace(namespace);
        validateTableNameName(tableName);

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
    public DeferredResult<?> update(IMDBClient client, String namespace, String tableName, String key, String json) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<?> upsert(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<?> upsert(IMDBClient client, String namespace, String tableName, String key, String json) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<?> replace(IMDBClient client, String namespace, String tableName, String key, List<Field> fieldList) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<?> replace(IMDBClient client, String namespace, String tableName, String key, String json) {
        // todo no used here
        return null;
    }

    @Override
    public DeferredResult<?> scan(IMDBClient client, String namespace, String tableName, String filter, List<String> fields) {
        RestValidator.validateNamespace(namespace);
        validateTableNameName(tableName);

        FilterModel filterModel = new FilterModel();
        if(filter != null)
            filterModel.setData(filter);

        List<KeyRecord> res = new ArrayList<>();

        try {
            client.scan(filterModel.getFilterRange(tableName), fields, (key, record) -> {
                res.add(new KeyRecord(key, record));
            }, false).get(Duration.ofMinutes(1));
        } catch (CheckedFutureException e) {
            e.printStackTrace();
            throw new ExceptionType.VIMDBRestClientError(e.getMessage());
        }

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(res, HttpStatus.OK));
        return returnValue;
    }

    @Override
    public DeferredResult<?> delete(IMDBClient client, String namespace, String tableName, String key, List<String> fieldNameList) {
        Logger.info("delete({}, {}, {}, {})", namespace, tableName, key, fieldNameList);
        RestValidator.validateNamespace(namespace);
        validateTableNameName(tableName);

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
    public DeferredResult<?> cmd(IMDBClient client, JsonNode req) {
        String cmd = req.get("cmd").asText();

        if(cmd == null) {
            throw new ExceptionType.BadRequestError();
        }
//        sqlParser.createStatement(cmd);

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
                "          \"value\": \"tooooooooooooooooooooooooooo long value 2\"\n" +
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
