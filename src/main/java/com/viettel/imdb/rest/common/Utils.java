package com.viettel.imdb.rest.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.common.Field;
import com.viettel.imdb.rest.exception.RestErrorCode;
import com.viettel.imdb.rest.exception.RestClientError;
import com.viettel.imdb.util.IMDBEncodeDecoder;
import io.trane.future.Future;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author longdt20
 * @since 16:12 05/12/2018
 */

public class Utils {

    private static final IMDBEncodeDecoder encodeDecoder = IMDBEncodeDecoder.getInstance();

    public static Map<String, List<Field>> convertJsonToMap(JsonNode jsonNode) {
        Map<String, List<Field>> records = new HashMap<>();

        jsonNode.fieldNames().forEachRemaining(key -> {
            System.out.println(key);
            JsonNode data = jsonNode.get(key);
            records.put(key, getFieldValue(data));
        });

        return records;
    }

    public static List<Field> getFieldValue(JsonNode jsonNode) {
        List<Field> value = new ArrayList<>();
        jsonNode.fieldNames().forEachRemaining(fieldName -> {
            JsonNode data = jsonNode.get(fieldName);
            value.add(new Field(fieldName, encodeDecoder.encodeJsonNode(data)));
        });
        return value;
    }

    public static JsonNode convertImdbFieldsToJsonNode(List<Field> fields) {
        JsonNode jsonNode = JsonNodeFactory.instance.objectNode();

        for (Field field : fields) {
            ((ObjectNode) jsonNode).set(field.getFieldName(), encodeDecoder.decodeJsonNode(field.getFieldValue()));
        }

        return jsonNode;
    }

    public static String convertToIndexField(String fieldName) {
        return fieldName.startsWith("$.") ? fieldName : "$." + fieldName;
    }

    public static DeferredResult<ResponseEntity<?>> INTERNAL_ERROR(String errorMessage) {
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

//        Map<String, Object> body = new HashMap<>();
//        body.put("error", errorMessage);
        result.setResult(new ResponseEntity<>(new RestClientError(RestErrorCode.SOMETHING_WENT_WRONG, errorMessage), HttpStatus.INTERNAL_SERVER_ERROR));

        return result;
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
                        returnValue.setResult(new ResponseEntity<>(result.getData(), result.getHttpStatus()))
                )
                .onFailure(throwable -> {
                    throwable.printStackTrace();
                    Map<String, Object> body = new HashMap<>();
                    body.put("error", throwable.getMessage());
                    returnValue.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
                });


        return returnValue;
    }
    public static DeferredResult<ResponseEntity<?>> restResultToDeferredResult2(Future<Result> restFuture) {
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        restFuture
                .onSuccess(result ->
                        returnValue.setResult(new ResponseEntity<>(result.getData(), result.getHttpStatus()))
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
