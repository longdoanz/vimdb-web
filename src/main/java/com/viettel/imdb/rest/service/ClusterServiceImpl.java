package com.viettel.imdb.rest.service;


import com.viettel.imdb.rest.model.AddClusterNodeRequest;
import com.viettel.imdb.rest.model.RemoveClusterNodeRequest;
import org.pmw.tinylog.Logger;
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
    @Override
    public DeferredResult<ResponseEntity<?>> getClusterInfo(List<String> nodes) {
        Logger.error("getClusterInfo({})", nodes);
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> addNode(AddClusterNodeRequest request) {
        Logger.error("addNode({})", request);
        return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> removeNode(RemoveClusterNodeRequest request) {
        Logger.error("removeNode({})", request);
        return null;
    }
}
