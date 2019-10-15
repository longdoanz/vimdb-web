package com.viettel.imdb.rest.mock.client;

import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.Transaction;
import com.viettel.imdb.common.*;
import com.viettel.imdb.secondaryindex.ResultSet;
import io.trane.future.Future;

import java.util.List;
import java.util.function.BiConsumer;

public class ClientSimulator implements IMDBClient {
    @Override
    public Future<Long> echo(long l) {
        return null;
    }

    @Override
    public Future<Void> createTable(String s) {
        return null;
    }

    @Override
    public Future<Void> dropTable(String s) {
        return null;
    }

    @Override
    public Future<Record> select(String s, String s1, List<String> list) {
        return null;
    }

    @Override
    public Future<Record> select(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Future<RecordSet> streamScan(Filter filter, List<String> list, BiConsumer<String, Record> biConsumer) {
        return null;
    }

    @Override
    public Future<ResultSet<KeyRecord>> scan(Filter filter, List<String> list, BiConsumer<String, Record> biConsumer, boolean b, int i) {
        return null;
    }

    @Override
    public Future<ResultSet<KeyRecord>> scan(Filter filter, List<String> list, BiConsumer<String, Record> biConsumer, boolean b) {
        return null;
    }

    @Override
    public Future<Void> createIndex(String s, String s1, ValueType valueType) {
        return null;
    }

    @Override
    public Future<Void> dropIndex(String s, String s1) {
        return null;
    }

    @Override
    public Future<Void> insert(String s, String s1, List<Field> list) {
        return null;
    }

    @Override
    public Future<Void> insert(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Future<Void> update(String s, String s1, List<Field> list) {
        return null;
    }

    @Override
    public Future<Void> update(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Future<Void> replace(String s, String s1, List<Field> list) {
        return null;
    }

    @Override
    public Future<Void> replace(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Future<Void> upsert(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Future<Void> upsert(String s, String s1, List<Field> list) {
        return null;
    }

    @Override
    public Future<Void> delete(String s, String s1, List<String> list) {
        return null;
    }

    @Override
    public Future<Void> delete(String s, String s1, String s2) {
        return null;
    }

    @Override
    public Transaction beginTransaction() {
        return null;
    }
}
