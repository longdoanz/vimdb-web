package com.viettel.imdb.rest.mock.server;

import com.viettel.imdb.common.Field;
import com.viettel.imdb.common.Filter;
import com.viettel.imdb.common.KeyRecord;
import com.viettel.imdb.common.Record;
import com.viettel.imdb.secondaryindex.ResultSet;
import io.trane.future.Future;

import java.util.List;
import java.util.function.BiConsumer;

public interface Storage {
    Future<Void> createTable(String tableName);
    Future<Void> dropTable(String tableName);

    Future<Record> select(String tableName, String key);
    Future<ResultSet<KeyRecord>> scan(Filter filter, List<String> fields, BiConsumer<String, Record> handler);
    Future<Void> insert(String tableName, String key, List<Field> fieldList);
    Future<Void> update(String tableName, String key, List<Field> fieldList);
    Future<Void> delete(String tableName, String key);
}
