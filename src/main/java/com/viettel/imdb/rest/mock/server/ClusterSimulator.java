package com.viettel.imdb.rest.mock.server;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.Record;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.core.security.User;
import com.viettel.imdb.rest.model.NamespaceInformation;
import io.trane.future.Future;

import java.util.*;

public class ClusterSimulator implements Storage, Security {
    private final String NS_DEFAULT = "namespace";
    private List<NodeSimulator> nodes;
    private int replicationFactor;
    private List<ClusterSimulator> drClusters;

    private DataStorage storage;
    Security security;

    public ClusterSimulator() {
        nodes = new ArrayList<>();
        drClusters = new ArrayList<>();
        replicationFactor = 2;

        storage = new DataStorage();
        security = SecurityImpl.getInstance();
    }

    public ClusterSimulator(boolean defaultDRCluster) {
        initDefaultDRCluster();
        storage = new DataStorage();
        security = SecurityImpl.getInstance();
    }

    public ClusterSimulator(List<NodeSimulator> nodes) {
        this.nodes = nodes;
        drClusters = new ArrayList<>();
        replicationFactor = 2;
        storage = new DataStorage();
        security = SecurityImpl.getInstance();
    }

    public void setNodes(List<NodeSimulator> nodes) {
        this.nodes = nodes;
    }

    public void initDefaultDRCluster() {
        drClusters = new ArrayList<>();
        ClusterSimulator dr01 = new ClusterSimulator();
        drClusters.add(dr01);
    }


    public boolean addNode(NodeSimulator node, boolean... currently) {
        if(nodes.contains(node)) {
            return false;
        }
        if(node.start()) {
            // Simulate like cluster remove node after 50HB
            if(currently.length != 0 && currently[0]) {
                nodes.add(node);
                return true;
            }

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    nodes.add(node);
                    timer.cancel();
                }
            }, 5000);
        }
        return true;
    }

    public void removeNode(NodeSimulator node) {
        if(node.stop()) {
            // Simulate like cluster remove node after 50HB
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    nodes.add(node);
                    timer.cancel();
                }
            }, 15000);
        }
    }

    @Override
    public Future<Void> createTable(String tableName) {
        return storage.createTable(tableName);
    }

    @Override
    public Future<Void> dropTable(String tableName) {
        return storage.dropTable(tableName);
    }

    @Override
    public Future<Record> select(String tableName, String key) {
        return storage.select(tableName, key);
    }

    @Override
    public Future<Void> insert(String tableName, String key, Record record) {
        return storage.insert(tableName, key, record);
    }

    @Override
    public Future<Void> update(String tableName, String key, Record record) {
        return storage.update(tableName, key, record);
    }

    @Override
    public Future<Void> delete(String tableName, String key) {
        return storage.delete(tableName, key);
    }


    public List<NamespaceInformation> getNamespaces() {
        NamespaceInformation namespaceInformation = new NamespaceInformation();
        namespaceInformation.setName(NS_DEFAULT);

        List<NamespaceInformation.TableInformation> tableInfo = namespaceInformation.getTables();
        storage.getData().forEach((tableName, tableData) -> {
            namespaceInformation.addTableInfo(tableName, tableData.data.size());
        });
        return new ArrayList<NamespaceInformation>() {{
            add(namespaceInformation);
        }};
    }

    ///==================================================================================
    /// SECURITY - START
    ///==================================================================================

    @Override
    public Future<List<User>> getAllUsers() {
        return security.getAllUsers();
    }

    @Override
    public Future<List<Role>> getAllRoles() {
        return security.getAllRoles();
    }

    @Override
    public Future<Void> changePassword(String username, byte[] newPassword) {
        return security.changePassword(username, newPassword);
    }

    @Override
    public Future<User> readUser(String username) {
        return security.readUser(username);
    }

    @Override
    public Future<Void> createUser(String username, byte[] password, List<String> roleNameList) {
        return security.createUser(username, password, roleNameList);
    }

    @Override
    public Future<Void> updateUser(String username, List<String> roleNameList) {
        return security.updateUser(username, roleNameList);
    }

    @Override
    public Future<Void> deleteUser(String username) {
        return security.deleteUser(username);
    }

    @Override
    public Future<Role> readRole(String rolename) {
        return security.readRole(rolename);
    }

    @Override
    public Future<Void> createRole(String roleName, List<String> privilegeList) {
        return security.createRole(roleName, privilegeList);
    }

    @Override
    public Future<Void> updateRole(String roleName, List<String> privilegeList) {
        return security.updateRole(roleName, privilegeList);
    }

    @Override
    public Future<Void> deleteRole(String roleName) {
        return security.deleteRole(roleName);
    }

    @Override
    public Future<Void> revokeAllRole(String roleName) {
        return security.revokeAllRole(roleName);
    }

    ///==================================================================================
    /// SECURITY - END
    ///==================================================================================
}
