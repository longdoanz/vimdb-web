package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.model.AddClusterNodeRequest;
import com.viettel.imdb.rest.model.RemoveClusterNodeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

/**
 * @author quannh22
 * @since 09/09/2019
 */
public interface ClusterService {
    DeferredResult<ResponseEntity<?>> getClusterInfo(List<String> nodes);

    DeferredResult<ResponseEntity<?>> addNode(AddClusterNodeRequest request);

    DeferredResult<ResponseEntity<?>> removeNode(RemoveClusterNodeRequest request);
}
