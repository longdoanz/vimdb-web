package com.viettel.imdb.rest.service;


import com.viettel.imdb.rest.RestErrorCode;
import com.viettel.imdb.rest.common.Translator;
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
        ClusterInfo clusterInfo = cluster.getClusterInfo(nodes);
        DeferredResult returnValue = new DeferredResult<>();
        returnValue.setResult(clusterInfo);
        return returnValue;
        //return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> addNode(AddClusterNodeRequest request) {
//        Logger.info("addNode: {}", request);

        AddClusterNodeRequest.NewClusterNodeServerInfo nodeInfo = request.getVimdbServerInfo();
        boolean status;
        if (nodeInfo.isDefaultConfigFile()) {
            status = cluster.addNode(new NodeSimulatorImpl(request.getSshInfo().getIp(), 10000));
        } else {
            String config = nodeInfo.getNewConfigContent();
            if (config == null || config.isEmpty())
                throw new ExceptionType.BadRequestError("Config content is required");

            Pattern pattern = Pattern.compile("host.*\"([0-9.]*)\".*");
            Matcher matcher = pattern.matcher(config);
            if (!matcher.find())
                throw new ExceptionType.BadRequestError("HOST_PARAM_REQUIRED");

            String ip = matcher.group(1);
            System.err.println("ip: " + ip);

            pattern = Pattern.compile("port.*=\\s*(\\d*).*");
            matcher = pattern.matcher(config);

            if (!matcher.find())
                throw new ExceptionType.BadRequestError("PORT_PARAM_REQUIRED");

            int port;
            try {
                System.out.println(matcher.group(1));
                port = Integer.parseInt(matcher.group(1));
            } catch (Exception ex) {
                throw new ExceptionType.BadRequestError("PORT_MUST_BE_INTEGER");
            }

            status = cluster.addNode(new NodeSimulatorImpl(ip, port));
        }


        RestClientError clientError = null;
        if (!status)
            throw new ExceptionType.BadRequestError("NODE_EXISTED");

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(clientError, HttpStatus.ACCEPTED));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> removeNode(RemoveClusterNodeRequest request) {
        if (request.getUsername().isEmpty() || request.getPassword().isEmpty()) {
            throw new ExceptionType.BadRequestError("Username and password is required");
        }
        boolean removed = false;
        if (request.getUsername().equals("admin") && request.getPassword().equals("admin"))
            removed = cluster.removeNode(new NodeSimulatorImpl(request.getHost(), request.getPort()));
        else
            throw new ExceptionType.BadRequestError("Username or password do not correct");
        RestClientError clientError = null;
        if (!removed) {
            clientError = new RestClientError(RestErrorCode.NODE_NOT_EXIST, Translator.toLocale("NODE_DO_NOT_EXIST"));
        }

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        returnValue.setResult(new ResponseEntity<>(clientError, removed ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST));

        return returnValue;
    }
}
