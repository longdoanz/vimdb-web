package com.viettel.imdb.rest.mock.server;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.Record;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.core.security.User;
import com.viettel.imdb.rest.model.ClusterInfo;
import com.viettel.imdb.rest.model.NamespaceInformation;
import io.trane.future.Future;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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

    public ClusterInfo getClusterInfo(List<String> nodesHost) {
        ClusterInfo clusterInfo = new ClusterInfo();
        clusterInfo.setNodeCount(nodes.size());
        clusterInfo.setReplicationFactor(replicationFactor);
        clusterInfo.setVersion("a.b.c");
        clusterInfo.setUptime( new Random().nextInt(100));
        clusterInfo.setMonitorNodeCount(new Random().nextInt(nodes.size()));

        ClusterInfo.ClusterDRInfo clusterDRInfo = clusterInfo.new ClusterDRInfo();
        clusterDRInfo.setDb("vIMDB");
        clusterDRInfo.setMethod("method");
        clusterDRInfo.setTotalOps(ThreadLocalRandom.current().nextLong());
        clusterDRInfo.setOpsRead(ThreadLocalRandom.current().nextLong());
        clusterDRInfo.setOpsTransferred(ThreadLocalRandom.current().nextLong());
        clusterInfo.setDrInfo(clusterDRInfo);

        List<ClusterInfo.ClusterNodeInfo> nodesInfo = new ArrayList<ClusterInfo.ClusterNodeInfo>();
        for(int i = 0; i < nodesHost.size(); i++){
            int index = 0;
            for(index = 0; index < nodes.size(); index++){
                String node = nodes.get(index).getHost()+":"+nodes.get(i).getPort();
                System.out.println(node  +   nodesHost.get(i));
                if(node.equals(nodesHost.get(i))) break;
            }
            if(index == nodes.size()) continue;


            ClusterInfo.ClusterNodeInfo clusterNodeInfo = clusterInfo.new ClusterNodeInfo();
            clusterNodeInfo.setName("node"+index);
            clusterNodeInfo.setIp(nodes.get(index).getHost());
            clusterNodeInfo.setVersion("a.b.c.Z");
            clusterNodeInfo.setUptime(new Random().nextInt(100));
            clusterNodeInfo.setOs("CentOS");
            clusterNodeInfo.setHeartbeatSent(ThreadLocalRandom.current().nextLong(200000));
            clusterNodeInfo.setHearbeatReceived(ThreadLocalRandom.current().nextLong(200000));
            clusterNodeInfo.setRamUsagePercentage(new Random().nextFloat()*100);
            clusterNodeInfo.setDiskUsagePercentage(new Random().nextFloat()*100);

            ClusterInfo.ClusterNodeDataInfo clusterNodeDataInfo = clusterInfo.new ClusterNodeDataInfo();
            clusterNodeDataInfo.setTables(ThreadLocalRandom.current().nextLong(2000));
            clusterNodeDataInfo.setRecords(ThreadLocalRandom.current().nextLong(200000));
            clusterNodeDataInfo.setMaster(ThreadLocalRandom.current().nextLong(20));
            clusterNodeInfo.setData(clusterNodeDataInfo);

            ClusterInfo.ClusterNodePerformanceInfo clusterNodePerformanceInfo = clusterInfo.new ClusterNodePerformanceInfo();
            clusterNodePerformanceInfo.setWrite(ThreadLocalRandom.current().nextLong(200000));
            clusterNodePerformanceInfo.setWriteSuccess(ThreadLocalRandom.current().nextLong(200000));
            clusterNodePerformanceInfo.setRead(ThreadLocalRandom.current().nextLong(200000));
            clusterNodePerformanceInfo.setWriteSuccess(ThreadLocalRandom.current().nextLong(200000));
            clusterNodePerformanceInfo.setDelete(ThreadLocalRandom.current().nextLong(200000));
            clusterNodePerformanceInfo.setDeleteSuccess(ThreadLocalRandom.current().nextLong(200000));
            clusterNodePerformanceInfo.setScan(ThreadLocalRandom.current().nextLong(200000));
            clusterNodePerformanceInfo.setScanSuccess(ThreadLocalRandom.current().nextLong(200000));
            clusterNodeInfo.setPerformance(clusterNodePerformanceInfo);

            nodesInfo.add(clusterNodeInfo);
        }
        clusterInfo.setNodes(nodesInfo);


        return clusterInfo;
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
