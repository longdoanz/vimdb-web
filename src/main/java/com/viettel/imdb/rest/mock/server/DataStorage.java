package com.viettel.imdb.rest.mock.server;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.*;
import com.viettel.imdb.rest.exception.ExceptionType;
import com.viettel.imdb.secondaryindex.ResultSet;
import io.trane.future.Future;
import io.trane.future.Promise;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class DataStorage implements Storage {

    public DB mapDB;
    //    Map<String, List<String>> namespaces;
    private Map<String, TableData> data;
    private ConcurrentMap tableStore;
    private static String FILE_NAME_STORE = "TableStore";
    AtomicReference<List<String>> pendingRemove;

    public DataStorage() {
        try {
            mapDB = DBMaker.fileDB(FILE_NAME_STORE + ".db").checksumHeaderBypass().make();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                Files.delete(Paths.get(FILE_NAME_STORE + ".db"));
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
            mapDB = DBMaker.fileDB(FILE_NAME_STORE + ".db").checksumHeaderBypass().make();
        }
        data = new ConcurrentHashMap<>();
        tableStore = mapDB.hashMap("map").createOrOpen();
        tableStore.keySet().forEach(key -> {
            data.put((String) key, new TableData((String) key));
        });

        pendingRemove = new AtomicReference<>(new ArrayList<>());
    }

    public Map<String, TableData> getData() {
        return data;
    }

    public Future<Void> createTable(String tableName) {
        Promise<Void> future = Promise.apply();
        if (tableName == null || tableName.equals(FILE_NAME_STORE)) {
            future.setException(new ClientException(ErrorCode.TABLENAME_INVALID));
            return future;
        }
        if (data == null || data.get(tableName) != null || pendingRemove.get().contains(tableName)) {
            future.setException(new ClientException(ErrorCode.TABLE_EXIST));
        } else {
            data.put(tableName, new TableData(tableName));
            tableStore.put(tableName, true);
            future.setValue(null);
        }
        return future;
    }

    public Future<Void> dropTable(String tableName) {
        Promise<Void> future = Promise.apply();
        if (tableName == null || data.get(tableName) == null || pendingRemove.get().contains(tableName)) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            pendingRemove.get().add(tableName);
            data.get(tableName).remove();
            tableStore.remove(tableName);
            data.remove(tableName);
            pendingRemove.get().remove(tableName);

            future.setValue(null);
        }
        return future;
    }

    @Override
    public Future<Record> select(String tableName, String key) {
        Promise<Record> future = Promise.apply();
        if (data.get(tableName) == null) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            Record record = data.get(tableName).get(key);
            if (record == null) {
                future.setException(new ClientException(ErrorCode.KEY_NOT_EXIST));
            } else
                future.setValue(data.get(tableName).get(key));
        }
        return future;
    }

    @Override
    public Future<ResultSet<KeyRecord>> scan(Filter filter, List<String> fields, BiConsumer<String, Record> handler) {
        Promise<ResultSet<KeyRecord>> future = Promise.apply();
        TableData res = data.get(filter.getTableName());
        if (res == null) {
            throw new ExceptionType.BadRequestError(ErrorCode.TABLE_NOT_EXIST, "Table not found");
        }
        res.getKeySet().forEach((key) -> {
            handler.accept(key, res.get(key));
        });
        future.setValue(null);

        return future;
    }

    @Override
    public Future<Void> insert(String tableName, String key, List<Field> fieldList) {
        Promise<Void> future = Promise.apply();
        if (data.get(tableName) == null) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            data.get(tableName).insert(future, key, fieldList);
        }
        return future;
    }

    @Override
    public Future<Void> update(String tableName, String key, List<Field> fieldList) {
        Promise<Void> future = Promise.apply();
        if (data.get(tableName) == null) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            data.get(tableName).update(future, key, fieldList);
        }
        return future;
    }

    @Override
    public Future<Void> delete(String tableName, String key) {
        Promise<Void> future = Promise.apply();
        if (data.get(tableName) == null) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            data.get(tableName).delete(future, key);
        }
        return future;
    }


    public void close() {
        data.values().forEach(TableData::close);
        mapDB.close();
    }
}
