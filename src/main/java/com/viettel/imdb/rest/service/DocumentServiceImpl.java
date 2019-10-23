package com.viettel.imdb.rest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.common.Filter;
import com.viettel.imdb.rest.RestErrorCode;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.common.Utils;
import com.viettel.imdb.rest.model.FilterModel;
import io.trane.future.Future;
import io.trane.future.Promise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.viettel.imdb.rest.common.Utils.getFieldValue;

@Component
public class DocumentServiceImpl implements DocumentService {

    private final
    IMDBClient imdbClient;

    @Autowired
    public DocumentServiceImpl(IMDBClient imdbClient) {
        this.imdbClient = imdbClient;
    }

    private void handleException(Promise<Result> future, Throwable throwable) {
        com.viettel.imdb.ErrorCode err = ((ClientException) throwable).getErrorCode();
        Result res = new Result();
        switch (err) {
            case KEY_INVALID:
            case KEY_LENGTH_INVALID:
            case SEC_INDEX_NOT_EXIST:
            case KEY_EXIST:
            case FIELDNAME_INVALID:
            case FIELDNAME_LENGTH_INVALID:
            case TABLE_OPERATION_TIMEOUT:
            case CONFLICT_OPERATION_ON_KEY:
                res.setHttpStatus(HttpStatus.BAD_REQUEST);
                break;
            case TABLE_NOT_EXIST:
            case KEY_NOT_EXIST:
                res.setHttpStatus(HttpStatus.NOT_FOUND);
                break;
            default:
                res.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        res.setMessage(err.name());
        future.setValue(res);
    }

    @Override
    public Future<Result> scan(IMDBClient imdbClient, String db, String tableName, FilterModel filter, String fields) {
        Promise<Result> future = Promise.apply();


        List<String> scanFieldNameList = null;
        if (!fields.isEmpty()) {
            scanFieldNameList = Arrays.asList(fields.split(","));
        }

        Filter filterRange = filter.getFilterRange(tableName);

        imdbClient.scan(filterRange, scanFieldNameList, null, true).onSuccess(keyRecordResultSet -> {
            future.setValue(new Result(HttpStatus.OK, keyRecordResultSet.getRecordQueue()));
        }).onFailure(throwable -> {
            handleException(future, throwable);
        });
        return future;
    }

    public Future<Result> select(IMDBClient imdbClient, String db, String tableName, String key, String fields) {
        Promise<Result> future = Promise.apply();

        List<String> fieldNameList = null;
        if (!fields.isEmpty()) {
            fieldNameList = Arrays.asList(fields.replace(" ", "").split(","));
        }

        imdbClient.select(tableName, key, fieldNameList).onSuccess((records -> {
            System.out.println(records);
            Result result = new Result(HttpStatus.OK, records);
            future.setValue(result);
        })).onFailure(throwable -> {
            throwable.printStackTrace();
            handleException(future, throwable);
        });

        return future;
    }

    public Future<Result> insert(IMDBClient imdbClient, String db, String tableName, JsonNode jsonNode) {

        Promise<Result> future = Promise.apply();

        Result result = new Result();

        String KEY_PARAM = "key";
        String VALUE_PARAM = "record";

        if (!jsonNode.has(KEY_PARAM)) {
            result.setHttpStatus(HttpStatus.BAD_REQUEST);
            result.setMessage(RestErrorCode.PARAMETER_KEY_REQUIRED.name());
            future.setValue(result);
            return future;
        }
        if (!jsonNode.has(VALUE_PARAM)) {
            result.setHttpStatus(HttpStatus.BAD_REQUEST);
            result.setMessage(RestErrorCode.PARAMETER_RECORD_REQUIRED.name());
            future.setValue(result);
            return future;
        }

        String keyRecord = jsonNode.get(KEY_PARAM).asText();

        imdbClient.insert(tableName, keyRecord, Utils.getFieldValue(jsonNode.get(VALUE_PARAM))).onSuccess(aVoid -> {
            result.setHttpStatus(HttpStatus.CREATED);
            future.setValue(result);
        }).onFailure(throwable -> {
            handleException(future, throwable);
        });

        return future;
    }

    public Future<Result> update(IMDBClient imdbClient, String db, String tableName, String key, JsonNode jsonNode) {

        Promise<Result> future = Promise.apply();
        Result result = new Result();

        imdbClient.update(tableName, key, getFieldValue(jsonNode)).onSuccess(aVoid -> {
            result.setHttpStatus(HttpStatus.NO_CONTENT);
            future.setValue(result);
        }).onFailure(throwable -> {
            handleException(future, throwable);
        });

        return future;
    }


    public Future<Result> delete(IMDBClient imdbClient, String db, String tableName, String key) {

        Promise<Result> future = Promise.apply();
        Result result = new Result();

        imdbClient.delete(tableName, key).onSuccess(aVoid -> {
            result.setHttpStatus(HttpStatus.NO_CONTENT);
            future.setValue(result);

        }).onFailure(throwable -> {
            handleException(future, throwable);
        });

        return future;

    }

}
