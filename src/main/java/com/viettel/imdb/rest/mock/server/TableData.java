package com.viettel.imdb.rest.mock.server;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.Record;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableData {
    Map<String, Record> data;

    public TableData() {
        data = new ConcurrentHashMap<>();
    }

    public ErrorCode insert(String key, Record record) {
        if(data.get(key) != null) {
            return ErrorCode.KEY_EXIST;
        }
        data.put(key, record);
        return ErrorCode.NO_ERROR;
    }

    public ErrorCode update(String key, Record record) {
        if(data.get(key) == null) {
            return ErrorCode.KEY_NOT_EXIST;
        }
        data.replace(key, record);
        return ErrorCode.NO_ERROR;
    }

    public ErrorCode delete(String key) {
        if(data.get(key) == null) {
            return ErrorCode.KEY_NOT_EXIST;
        }
        data.remove(key);
        return ErrorCode.NO_ERROR;
    }
}
