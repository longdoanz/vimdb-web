package com.viettel.imdb.rest.mock;

import com.viettel.imdb.rest.mock.server.ClusterSimulator;
import com.viettel.imdb.rest.mock.server.NodeSimulator;
import com.viettel.imdb.rest.mock.server.NodeSimulatorImpl;
import org.testng.annotations.Test;

public class ClusterTest {

    @Test
    public void testNewCluster() {
        ClusterSimulator cluster = new ClusterSimulator();
        NodeSimulator node1 = new NodeSimulatorImpl("127.0.0.1", 10000);
        System.err.println(cluster.addNode(node1, true));;
        NodeSimulator node2 = new NodeSimulatorImpl("127.0.0.1", 100900);
        System.err.println(cluster.addNode(node1, true));;
        System.err.println(cluster.addNode(node2, true));;

    }
}
