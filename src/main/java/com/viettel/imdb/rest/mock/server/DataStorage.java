package com.viettel.imdb.rest.mock.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.*;
import com.viettel.imdb.rest.exception.ExceptionType;
import com.viettel.imdb.secondaryindex.ResultSet;
import com.viettel.imdb.util.IMDBEncodeDecoder;
import io.trane.future.Future;
import io.trane.future.Promise;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class DataStorage implements Storage {

    private static IMDBEncodeDecoder encodeDecoder = IMDBEncodeDecoder.getInstance();

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
        if (tableName == null) {
            future.setException(new ClientException(ErrorCode.TABLENAME_INVALID));
            return future;
        }
        if (data == null || data.get(tableName) != null) {
            future.setException(new ClientException(ErrorCode.TABLE_EXIST));
        } else {
            data.put(tableName, new TableData());
            future.setValue(null);
        }
        return future;
    }

    public Future<Void> dropTable(String tableName) {
        Promise<Void> future = Promise.apply();
        if (data.get(tableName) == null) {
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
        if (data.get(tableName) == null) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            Record record = data.get(tableName).data.get(key);
            if (record == null)
                future.setException(new ClientException(ErrorCode.KEY_NOT_EXIST));
            future.setValue(data.get(tableName).data.get(key));
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
        String filterInField = filter.getFieldName();
        if(filterInField != null && filterInField.startsWith("$."))
            filterInField = filterInField.substring(2);
        String finalFilterInField = filterInField;
        res.data.keySet().forEach((key) -> {
            Record record = res.data.get(key);
            if (finalFilterInField != null && !finalFilterInField.isEmpty()) {
                boolean found = false;
                for (Field field : record.getValue()) {
                    if(!field.getFieldName().equals(finalFilterInField))
                        continue;
                    JsonNode node = encodeDecoder.decodeJsonNode(field.getFieldValue());
                    if ((node.isInt() || node.isLong())
                            && node.longValue() >= filter.getBeginInLong()
                            && node.longValue() < filter.getEndInLong()) {
                        found = true;
                        break;
                    }
                }
                if(!found)
                    return;
            }
            handler.accept(key, record);
        });
        future.setValue(null);

        return future;
    }

    @Override
    public Future<Void> insert(String tableName, String key, Record record) {
        Promise<Void> future = Promise.apply();
        if (data.get(tableName) == null) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            data.get(tableName).insert(future, key, record);
        }
        return future;
    }

    @Override
    public Future<Void> update(String tableName, String key, Record record) {
        Promise<Void> future = Promise.apply();
        if (data.get(tableName) == null) {
            future.setException(new ClientException(ErrorCode.TABLE_NOT_EXIST));
        } else {
            data.get(tableName).update(future, key, record);
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


}
