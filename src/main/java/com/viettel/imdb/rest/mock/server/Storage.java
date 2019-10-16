package com.viettel.imdb.rest.mock.server;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.Record;
import io.trane.future.Future;

public interface Storage {
    Future<Void> createTable(String tableName);
    Future<Void> dropTable(String tableName);

    Future<Record> select(String tableName, String key);
    Future<Void> insert(String tableName, String key, Record record);
    Future<Void> update(String tableName, String key, Record record);
    Future<Void> delete(String tableName, String key);
}
