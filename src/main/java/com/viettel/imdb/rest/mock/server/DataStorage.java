package com.viettel.imdb.rest.mock.server;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.common.Record;
import io.trane.future.Future;
import io.trane.future.Promise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStorage implements Storage{


//    Map<String, List<String>> namespaces;
    private Map<String, TableData> data;


    public DataStorage() {
        data = new ConcurrentHashMap<>();
//        namespaces = new HashMap<>();
//        namespaces.put(NS_DEFAULT, new ArrayList<>());
    }

    public Map<String, TableData> getData() {
        return data;
    }

    public Future<Void> createTable(String tableName) {
        Promise<Void> future = Promise.apply();
        if(data == null || data.get(tableName) != null) {
            future.setException(new ClientException(ErrorCode.TABLE_EXIST));
        } else {
            data.put(tableName, new TableData());
            future.setValue(null);
        }
        return future;
    }

    public Future<Void> dropTable(String tableName) {
        Promise<Void> future = Promise.apply();
        if(data.get(tableName) == null) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            data.remove(tableName);
            future.setValue(null);
        }
        return future;
    }

    @Override
    public Future<Record> select(String tableName, String key) {
        Promise<Record> future = Promise.apply();
        if(data.get(tableName) == null) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            future.setValue(data.get(tableName).data.get(key));
        }
        return future;
    }

    @Override
    public Future<Void> insert(String tableName, String key, Record record) {
        Promise<Void> future = Promise.apply();
        if(data.get(tableName) == null) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            data.get(tableName).insert(key, record);
            future.setValue(null);
        }
        return future;
    }

    @Override
    public Future<Void> update(String tableName, String key, Record record) {
        Promise<Void> future = Promise.apply();
        if(data.get(tableName) == null) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            data.get(tableName).update(key, record);
            future.setValue(null);
        }
        return future;
    }

    @Override
    public Future<Void> delete(String tableName, String key) {
        Promise<Void> future = Promise.apply();
        if(data.get(tableName) == null) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            data.get(tableName).delete(key);
            future.setValue(null);
        }
        return future;
    }



}
