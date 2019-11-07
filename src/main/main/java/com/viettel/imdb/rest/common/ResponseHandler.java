package com.viettel.imdb.rest.common;

import com.viettel.imdb.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author longdt20
 * @since 14:48 05/12/2018
 */

public class ResponseHandler {



    /*public static void generateCreationResponse(DeferredResult<ResponseEntity> deferred, Result result) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", result.getHttpStatus());
        responseBody.put("error", errorCode.name());
        deferred.setResult(new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST));
    }*/

    public static void generateCreationResponse(DeferredResult<ResponseEntity> deferred, ErrorCode errorCode) {

        if (errorCode == ErrorCode.NO_ERROR) {
            deferred.setResult(new ResponseEntity<>(HttpStatus.CREATED));
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", HttpStatus.BAD_REQUEST.value());
            responseBody.put("error", errorCode.name());
            deferred.setResult(new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST));
        }
    }

    public static void generateDeleteResponse(DeferredResult<ResponseEntity> deferred, ErrorCode errorCode) {

        if (errorCode == ErrorCode.NO_ERROR) {
            deferred.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("error", errorCode.name());
            deferred.setResult(new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND));
        }
    }

    public static void generateResponse(DeferredResult<ResponseEntity> deferred, Result result) {
        Map<String, Object> responseBody = new HashMap<>();
        if (result.getMessage() != null) {
            responseBody.put("error", result.getMessage());
        }
        if (result.getData() != null) {
            responseBody.put("results", result.getData());
        }
        deferred.setResult(new ResponseEntity<>(responseBody, result.getHttpStatus()));
    }

    public static void generateInternalErrorResponse(DeferredResult<ResponseEntity> deferred) {
        Map<String, Object> body = new HashMap<>();
        deferred.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public static void generateInternalErrorResponse(DeferredResult<ResponseEntity> deferred, String msg) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", msg);
        deferred.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public static void generateValidatorError(DeferredResult<ResponseEntity> deferred, Enum err) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", err.name());
        deferred.setResult(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }
}
