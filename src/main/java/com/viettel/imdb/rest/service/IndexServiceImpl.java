package com.viettel.imdb.rest.service;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.model.IndexModel;
import com.viettel.imdb.rest.model.TableModel;
import io.trane.future.Future;
import io.trane.future.Promise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class IndexServiceImpl implements IndexService {

    @Autowired
    IMDBClient imdbClient;

    private void handleException(Promise<Result> future, Throwable throwable) {
        ErrorCode err = ((ClientException) throwable).getErrorCode();
        Result res = new Result();
        switch (err) {
            case SEC_INDEX_EXIST:
            case FIELDNAME_INVALID:
            case FIELDNAME_LENGTH_INVALID:
            case TABLE_OPERATION_TIMEOUT:
                res.setHttpStatus(HttpStatus.BAD_REQUEST);
                break;
            case TABLE_NOT_EXIST:
                res.setHttpStatus(HttpStatus.NOT_FOUND);
                break;
            default:
                res.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        res.setMessage(err.name());
        future.setValue(res);
    }

    @Override
    public Future<Result> createIndex(TableModel tableModel, IndexModel indexModel) {
        Promise<Result> future = Promise.apply();

        imdbClient.createIndex(tableModel.getTableName(), indexModel.getName(), indexModel.getType()).onSuccess(aVoid -> {
            future.setValue(new Result(HttpStatus.CREATED));
        }).onFailure(throwable -> {
            handleException(future, throwable);
        });
        return future;
    }

    @Override
    public Future<Result> dropIndex(TableModel tableModel, String fieldName) {
        Promise<Result> future = Promise.apply();
        Result res = new Result();

        try {
            tableModel.validateData();


            imdbClient.dropIndex(tableModel.getTableName(), fieldName).onSuccess(aVoid -> {
                future.setValue(new Result(HttpStatus.NO_CONTENT));
            }).onFailure(throwable -> {
                handleException(future, throwable);
            });
        } catch (ClientException ex) {
            res.setHttpStatus(HttpStatus.BAD_REQUEST);
            res.setMessage(ex.getMessage());
            future.setValue(res);
        }

        return future;
    }

}
