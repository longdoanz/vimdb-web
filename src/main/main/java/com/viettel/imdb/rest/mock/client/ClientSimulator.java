package com.viettel.imdb.rest.mock.client;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.Transaction;
import com.viettel.imdb.common.*;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.core.security.User;
import com.viettel.imdb.rest.mock.server.ClusterSimulator;
import com.viettel.imdb.rest.model.NamespaceInformation;
import com.viettel.imdb.rest.model.UserInfo;
import com.viettel.imdb.secondaryindex.ResultSet;
import io.trane.future.Future;
import io.trane.future.Promise;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ClientSimulator implements IMDBClient {
    ClusterSimulator cluster;

    public ClientSimulator(ClusterSimulator cluster) {
        this.cluster = cluster;
    }

    @Override
    public Future<Long> echo(long msg) {
        return null;
    }

    private void setFuture(Promise<Void> future, ErrorCode errorCode) {
        if(errorCode != ErrorCode.NO_ERROR) {
            future.setException(new ClientException(errorCode));
        }
        future.setValue(null);
    }

    private void setValue(Promise<Object> future, ErrorCode errorCode, Record record) {
        if(errorCode != ErrorCode.NO_ERROR) {
            future.setException(new ClientException(errorCode));
        }
        future.setValue(record);
    }


    @Override
    public Future<Void> createTable(String tableName) {
        return cluster.createTable(tableName);
    }

    @Override
    public Future<Void> dropTable(String tableName) {
        return cluster.dropTable(tableName);
    }

    @Override
    public Future<Record> select(String tableName, String key, List<String> fieldNameList) {
        return cluster.select(tableName, key);
    }

    @Override
    public Future<Record> select(String tableName, String key, String jsonData) {
        return cluster.select(tableName, key);
    }

    @Override
    public Future<RecordSet> streamScan(Filter filter, List<String> fieldNameList, BiConsumer<String, Record> recordHandler) {
        return null;
    }

    @Override
    public Future<ResultSet<KeyRecord>> scan(Filter filter, List<String> fieldNameList, BiConsumer<String, Record> recordHandler, boolean accumulate, int maxRecord) {
        return null;
    }

    @Override
    public Future<ResultSet<KeyRecord>> scan(Filter filter, List<String> fieldNameList, BiConsumer<String, Record> recordHandler, boolean accumulate) {
        return cluster.scan(filter, fieldNameList, recordHandler);
    }

    @Override
    public Future<Void> createIndex(String tableName, String fieldName, ValueType valueType) {
        return null;
    }

    @Override
    public Future<Void> dropIndex(String tableName, String fieldName) {
        return null;
    }

    @Override
    public Future<Void> insert(String tableName, String key, List<Field> fields) {
        return cluster.insert(tableName, key, new Record(fields));
    }

    @Override
    public Future<Void> insert(String tableName, String key, String jsonData) {
        return null;
    }

    @Override
    public Future<Void> update(String tableName, String key, List<Field> fields) {
        return cluster.update(tableName, key, new Record(fields));
    }

    @Override
    public Future<Void> update(String tableName, String key, String jsonData) {
        return null;
    }

    @Override
    public Future<Void> replace(String tableName, String key, List<Field> fields) {
        return null;
    }

    @Override
    public Future<Void> replace(String tableName, String key, String jsonData) {
        return null;
    }

    @Override
    public Future<Void> upsert(String tableName, String key, String jsonData) {
        return null;
    }

    @Override
    public Future<Void> upsert(String tableName, String key, List<Field> fields) {
        return null;
    }

    @Override
    public Future<Void> delete(String tableName, String key, List<String> fieldNameList) {
        return cluster.delete(tableName, key);
    }

    @Override
    public Future<Void> delete(String tableName, String key, String jsonData) {
        return cluster.delete(tableName, key);
    }

    @Override
    public Transaction beginTransaction() {
        return null;
    }

    @Override
    public Future<Void> changePassword(String s, byte[] bytes) {
        return cluster.changePassword(s, bytes);
    }

    @Override
    public Future<User> readUser(String s) {
        return cluster.readUser(s);
    }

    @Override
    public Future<Void> createUser(String s, byte[] bytes, List<String> list) {
        return cluster.createUser(s, bytes, list);
    }

    @Override
    public Future<Void> updateUser(String s, List<String> list) {
        return cluster.updateUser(s, list);
    }

    @Override
    public Future<Void> deleteUser(String s) {
        return cluster.deleteUser(s);
    }

    @Override
    public Future<Role> readRole(String s) {
        return cluster.readRole(s);
    }

    @Override
    public Future<Void> createRole(String s, List<String> list) {
        return cluster.createRole(s, list);
    }

    @Override
    public Future<Void> updateRole(String s, List<String> list) {
        return cluster.updateRole(s, list);
    }

    @Override
    public Future<Void> deleteRole(String s) {
        return cluster.deleteRole(s);
    }

    @Override
    public Future<Void> revokeAllRole(String s) {
        return cluster.revokeAllRole(s);
    }

    ///============================================
    /// FAKE APIs come here - NO NEED FUTURE
    ///============================================

    public Future<UserInfo> readUserInfo(String s) {
        return cluster.readUserinfo(s);
    }

    public List<NamespaceInformation> getNamespaces() {
        return cluster.getNamespaces();
    }


    public NamespaceInformation getTableList(String ns) {
        return cluster.getTableList(ns);
    }

    public Future<List<User>> getUsers() {
        return cluster.getAllUsers();
    }
    public Future<List<UserInfo>> getUsersInfo() {
        return cluster.getAllUsersInfo();
    }

    public Future<List<Role>> getRoles() {
        return cluster.getAllRoles();
    }

    public Future<List<String>> getAuditLogs() {
        return Future.value(new ArrayList<>());
    }
}
