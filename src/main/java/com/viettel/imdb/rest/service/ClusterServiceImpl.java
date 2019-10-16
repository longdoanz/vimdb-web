package com.viettel.imdb.rest.service;


import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.rest.mock.client.ClientSimulator;
import com.viettel.imdb.rest.mock.server.ClusterSimulator;
import com.viettel.imdb.rest.mock.server.NodeSimulatorImpl;
import com.viettel.imdb.rest.model.AddClusterNodeRequest;
import com.viettel.imdb.rest.model.ClusterInfo;
import com.viettel.imdb.rest.model.RemoveClusterNodeRequest;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * @author quannh22
 * @since 09/09/2019
 */
@Service
public class ClusterServiceImpl implements ClusterService {
//    private final IMDBClient client;

    private final ClusterSimulator cluster;

    @Autowired
    public ClusterServiceImpl(ClusterSimulator cluster) {
        this.cluster = cluster;
    }



    @Override
    public DeferredResult<ResponseEntity<?>> getClusterInfo(List<String> nodes) {
        Logger.error("getClusterInfo({})", nodes);
        ClusterInfo clusterInfo = cluster.getClusterInfo(nodes);
        DeferredResult returnValue = new DeferredResult<>();
        returnValue.setResult(clusterInfo);
        return returnValue;
        //return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> addNode(AddClusterNodeRequest request) {
        cluster.addNode(new NodeSimulatorImpl(request.getVimdbServerInfo().getHost(), request.getVimdbServerInfo().getPort()));

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.CREATED));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> removeNode(RemoveClusterNodeRequest request) {
        cluster.removeNode(new NodeSimulatorImpl(request.getVimdbDeleteNodeInfo().getHost(), request.getVimdbDeleteNodeInfo().getPort()));

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.NO_CONTENT));

        return returnValue;
    }
}
