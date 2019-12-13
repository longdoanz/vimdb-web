package com.viettel.imdb.rest.mock.server;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.common.Field;
import com.viettel.imdb.common.Record;
import io.trane.future.Promise;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableData {
    Map<String, Record> data;

    public TableData() {
        data = new ConcurrentHashMap<>();
    }


    public void insert(Promise<Void> future, String key, Record record) {
        if(data.get(key) != null) {
            future.setException(new ClientException(ErrorCode.KEY_EXIST));
            return;
        }
        data.put(key, record);
        future.setValue(null);
    }

    public void update(Promise<Void> future, String key, Record record) {
        if(data.get(key) == null) {
            future.setException(new ClientException(ErrorCode.KEY_NOT_EXIST));
            return;
        }
        data.replace(key, record);
        future.setValue(null);
    }

    public void delete(Promise<Void> future, String key) {
        if(data.get(key) == null) {
            future.setException(new ClientException(ErrorCode.KEY_NOT_EXIST));
            return;
        }
        data.remove(key);
        future.setValue(null);
    }
}
