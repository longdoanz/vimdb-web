package com.viettel.imdb.rest.service;


import com.viettel.imdb.rest.RestErrorCode;
import com.viettel.imdb.rest.exception.ExceptionType;
import com.viettel.imdb.rest.mock.server.ClusterSimulator;
import com.viettel.imdb.rest.mock.server.NodeSimulatorImpl;
import com.viettel.imdb.rest.model.AddClusterNodeRequest;
import com.viettel.imdb.rest.model.ClusterInfo;
import com.viettel.imdb.rest.model.RemoveClusterNodeRequest;
import com.viettel.imdb.rest.model.RestClientError;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Logger.info("getClusterInfo({})", nodes);
        ClusterInfo clusterInfo = cluster.getClusterInfo(nodes);
        DeferredResult returnValue = new DeferredResult<>();
        returnValue.setResult(clusterInfo);
        return returnValue;
        //return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> addNode(AddClusterNodeRequest request) {
        Logger.info("addNode: {}", request);

        AddClusterNodeRequest.NewClusterNodeServerInfo nodeInfo = request.getVimdbServerInfo();
        boolean status;
        if (nodeInfo.isDefaultConfigFile()) {
            status = cluster.addNode(new NodeSimulatorImpl(request.getSshInfo().getIp(), 10000));
        } else {
            String config = nodeInfo.getNewConfigContent();
            if (config == null || config.isEmpty())
                throw new ExceptionType.BadRequestError("Config content is required");

            Pattern pattern = Pattern.compile("host.*=\\s*(\\d*)");
            Matcher matcher = pattern.matcher(config);
            if (!matcher.matches())
                throw new ExceptionType.BadRequestError("Host parameter in config is required");

            String ip = matcher.group(0);

            pattern = Pattern.compile("port.*=\\s*(\\d*)");
            matcher = pattern.matcher(config);

            if (!matcher.matches())
                throw new ExceptionType.BadRequestError("Port parameter in config is required");

            int port;
            try {
                port = Integer.parseInt(matcher.group(0));
            } catch (Exception ex) {
                throw new ExceptionType.BadRequestError("Port parameter must be integer");
            }

            status = cluster.addNode(new NodeSimulatorImpl(ip, port));
        }


        RestClientError clientError = null;
        if (!status)
            throw new ExceptionType.BadRequestError("Node existed");

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(clientError, HttpStatus.ACCEPTED));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> removeNode(RemoveClusterNodeRequest request) {
        boolean removed = cluster.removeNode(new NodeSimulatorImpl(request.getHost(), request.getPort()));

        RestClientError clientError = null;
        if (!removed) {
            clientError = new RestClientError(RestErrorCode.NODE_NOT_EXIST, "Node do not exist");
        }

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(clientError, removed ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST));

        return returnValue;
    }
}
