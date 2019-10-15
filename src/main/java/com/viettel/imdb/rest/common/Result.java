package com.viettel.imdb.rest.common;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class Result {

    private HttpStatus httpStatus;
    private int code;
    private String message;
    private Object data;
    private boolean success;

    public Result() {
    }

    public Result(HttpStatus status) {
        this.httpStatus = status;
    }
    
    public Result(HttpStatus status, String msg, Object objData) {
        this.httpStatus = status;
        this.message = msg;
        this.data = objData;
    }

    public Result(HttpStatus status, String msg) {
        this.httpStatus = status;
        this.message = msg;
    }

    public Result(HttpStatus status, Object data) {
        this.httpStatus = status;
        this.data = data;
    }

    public Result(boolean success, String message) {
        this.message = message;
        this.success = success;
    }

    public Result(boolean success, Object data) {
        this.data = data;
        this.success = success;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map getResponse() {
        if (httpStatus == HttpStatus.CREATED || httpStatus == HttpStatus.NO_CONTENT) {
            return null;
        }
        Map<String, Object> res = new HashMap<>();
        if (this.message != null) {
            res.put("error", this.message);
        }
        if (this.data != null) {
            res.put("results", this.data);
        }
        return res;
    }

    /*public Map getRawResponse() {
        if (httpStatus == HttpStatus.CREATED || httpStatus == HttpStatus.NO_CONTENT) {
            return null;
        }
        Map<String, Object> res = new HashMap<>();
        if (this.message != null) {
            res.put("error", this.message);
        }
        return res;
    }*/
}
