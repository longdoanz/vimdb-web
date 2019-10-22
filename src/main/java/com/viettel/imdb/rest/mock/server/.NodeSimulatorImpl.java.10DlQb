package com.viettel.imdb.rest.mock.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeSimulatorImpl implements NodeSimulator {
    private String host;
    private int port;

    private NodeState state;

//    private Map<String, DataStorage> storage;

    public NodeSimulatorImpl(String host, int port) {
        this.host = host;
        this.port = port;
        state = NodeState.STOP;
    }



    @Override
    public boolean start() {
        state = NodeState.RUNNING;
        return true;
    }

    @Override
    public boolean stop() {
        state = NodeState.RUNNING;
        return true;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    public boolean equals(Object o) {
        if(this == o) return true;
        NodeSimulatorImpl node = (NodeSimulatorImpl) o;
        return this.host != null && this.host.equals(node.host) && this.port == node.port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String getAddress() {
        return host + ":" + port;
    }

    @Override
    public String toString() {
        return "Node{host=" + host + ", port=" + port + "}";
    }

    /*@Override
    public int hashCode() {
    }*/


}
