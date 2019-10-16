package com.viettel.imdb.rest.service;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.rest.common.RestValidator;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.common.Utils;
import com.viettel.imdb.rest.model.MetricResponse;
import com.viettel.imdb.rest.model.StatisticFilter;
import com.viettel.imdb.rest.model.StatisticResponse;
import com.viettel.imdb.rest.util.StatisticClient;
import io.trane.future.Future;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;

import static com.viettel.imdb.rest.common.Utils.restResultToDeferredResult;
import static com.viettel.imdb.rest.common.Utils.throwableToHttpStatus;

@Service
public class StatisticServiceImpl implements StatisticService{
    private final StatisticClient client;

    @Autowired
    public StatisticServiceImpl(StatisticClient client) {
        this.client = client;
    }

    public DeferredResult<ResponseEntity<?>> getMetrics(String servers){
        try {
            Logger.error("Get metric {}", servers);
            if(servers == null)
                servers = "";
            String[] serverArr = servers.trim().split(",");
            List<String> serverList = Arrays.asList(serverArr);
            Logger.error("Get metric {}", serverList);

            Future<List<MetricResponse>> getMetricsFuture = client.getMetrics(serverList);
            Future<Result> resultFuture = getMetricsFuture
                    .map(metricList -> new Result(HttpStatus.OK, metricList))
                    .rescue(throwable -> throwableToHttpStatus(throwable));
            return restResultToDeferredResult(resultFuture);
        } catch (Exception ex){
            return restResultToDeferredResult(Future.exception(ex));
        }
    }
    public DeferredResult<ResponseEntity<?>> getStatistics(String servers, String metrics){
        try {
            Logger.error("Get statistics servers {} --- metrics {}", servers, metrics);
            List<String> serverList;
            List<String> metricList;
            if(servers == null || servers.isEmpty()) {
                serverList = new ArrayList<>();
            } else {
                String[] serverArr = servers.trim().split(",");
                serverList = Arrays.asList(serverArr);
            }
            if(metrics == null || metrics.isEmpty()) {
                metricList = new ArrayList<>();
            } else {
                String[] metricArr = metrics.trim().split(",");
                metricList = Arrays.asList(metricArr);
            }


            Future<List<StatisticResponse>> getMetricsFuture = client.getStatistics(serverList, metricList);
            Future<Result> resultFuture = getMetricsFuture
                    .map(metricResponseList -> new Result(HttpStatus.OK, metricResponseList))
                    .rescue(throwable -> throwableToHttpStatus(throwable));
            return restResultToDeferredResult(resultFuture);
        } catch (Exception ex){
            return restResultToDeferredResult(Future.exception(ex));
        }
    }



    //==========================================================
    // Utility functions
    //==========================================================

    public static Future<Result> throwableToHttpStatus(Throwable throwable) {
        if(throwable instanceof ClientException) {
            ErrorCode errorCode = ((ClientException)throwable).getErrorCode();
            switch (errorCode) {
                case TABLE_EXIST:
                case TABLENAME_LENGTH_INVALID:
                case TABLENAME_INVALID:
                case TABLE_OPERATION_TIMEOUT:
                case TABLE_IN_USE:
                case KEY_INVALID:
                case KEY_LENGTH_INVALID:
                case SEC_INDEX_NOT_EXIST:
                case KEY_EXIST:
                case CONFLICT_OPERATION_ON_KEY:
                case SEC_INDEX_EXIST:
                case FIELDNAME_INVALID:
                case FIELDNAME_LENGTH_INVALID:
                    return Future.value(new Result(HttpStatus.BAD_REQUEST, errorCode.name()));
                case TABLE_NOT_EXIST:
                case KEY_NOT_EXIST:
                    return Future.value(new Result(HttpStatus.NOT_FOUND, errorCode.name()));
                default:
                    return Future.value(new Result(HttpStatus.INTERNAL_SERVER_ERROR, errorCode.name()));
            }
        } else {
            return Future.value(new Result(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"));
        }

    }


    public static DeferredResult<ResponseEntity<?>> restResultToDeferredResult(Future<Result> restFuture) {
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        restFuture
                .onSuccess(result ->
                        returnValue.setResult(new ResponseEntity<>(result.getResponse(), result.getHttpStatus()))
                )
                .onFailure(throwable -> {
                    throwable.printStackTrace();
                    Map<String, Object> body = new HashMap<>();
                    body.put("error", throwable.getMessage());
                    returnValue.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
                });


        return returnValue;
    }
}
