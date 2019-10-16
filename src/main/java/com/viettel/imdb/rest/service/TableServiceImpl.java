package com.viettel.imdb.rest.service;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.model.TableModel;
import io.trane.future.Future;
import io.trane.future.Promise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TableServiceImpl implements TableService {

    private final
    IMDBClient imdbClient;

    @Autowired
    public TableServiceImpl(IMDBClient imdbClient) {
        this.imdbClient = imdbClient;
    }

    private void generateHttpStatus(Result res, ErrorCode err) {
        switch (err) {
            case TABLE_EXIST:
            case TABLENAME_LENGTH_INVALID:
            case TABLENAME_INVALID:
            case TABLE_OPERATION_TIMEOUT:
            case TABLE_IN_USE:
                res.setHttpStatus(HttpStatus.BAD_REQUEST);
                break;
            case TABLE_NOT_EXIST:
                res.setHttpStatus(HttpStatus.NOT_FOUND);
                break;
            default:
                res.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        res.setMessage(err.name());
    }


    public Future<Result> createTable(TableModel tableModel) {
        Promise<Result> future = Promise.apply();
        Result res = new Result();

        imdbClient.createTable(tableModel.getTableName()).onSuccess(aVoid -> {
            res.setHttpStatus(HttpStatus.CREATED);
            future.setValue(res);
        }).onFailure(throwable -> {
            generateHttpStatus(res, ((ClientException) throwable).getErrorCode());

            future.setValue(res);
        });

        return future;
    }

    public Future<Result> dropTable(TableModel tableModel) {
        Promise<Result> future = Promise.apply();
        Result res = new Result();

        imdbClient.dropTable(tableModel.getTableName()).onSuccess(aVoid -> {
            res.setHttpStatus(HttpStatus.NO_CONTENT);
            future.setValue(res);

        }).onFailure(throwable -> {
            generateHttpStatus(res, ((ClientException) throwable).getErrorCode());

            future.setValue(res);
        });


        return future;
    }

}
