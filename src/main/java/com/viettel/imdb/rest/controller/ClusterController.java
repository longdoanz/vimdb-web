package com.viettel.imdb.rest.controller;

import com.viettel.imdb.rest.domain.RestClientError;
import com.viettel.imdb.rest.model.AddClusterNodeRequest;
import com.viettel.imdb.rest.model.RemoveClusterNodeRequest;
import com.viettel.imdb.rest.service.ClusterService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author quannh22
 * @since 08/08/2019
 */
@Api(tags = "Cluster related operations", description = "Perform operations related to cluster")
@RestController
@RequestMapping("/v1/cluster")
public class ClusterController {
    private static final String GET_CLUSTER_INFO_NOTES = "Get cluster info with statistics of input nodes";
    private static final String NODE_LIST_NOTES = "Input node list to get statistics";
    private static final String ADD_NODE_TO_CLUSTER = "Start a node and join the node to cluster";
    private static final String ADD_CLUSTER_NODE_REQUEST_NOTES = "Request with input node info to add to cluster";
    private static final String REMOVE_NODE_FROM_CLUSTER = "Stop a node and remove the node from cluster";
    private static final String REMOVE_CLUSTER_NODE_REQUEST_NOTES = "Request with input node info to remove from cluster";
    /**
     * Cluster Service to mainly serve request from this controller
     */
    @Autowired private ClusterService service;

    @RequestMapping(method = RequestMethod.GET, value = "/info")
    @ApiOperation(value = GET_CLUSTER_INFO_NOTES, nickname = "getClusterInfo")
        @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> getClusterInfo(
        //@ApiParam(required = false, value = NODE_LIST_NOTES) @RequestParam(value = "nodes", required=false) List<String> nodes
        @ApiParam(required = false, value = NODE_LIST_NOTES) @RequestParam(value = "nodes", required=false) List<String> nodes
    ) {
        //List<String> nodes = new ArrayList<String>();
        return service.getClusterInfo(nodes);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add_node")
    @ApiOperation(value = ADD_NODE_TO_CLUSTER, nickname = "addNode")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> addNode(
            @ApiParam(required = true, value = ADD_CLUSTER_NODE_REQUEST_NOTES) @RequestBody AddClusterNodeRequest request
    ) {
        return service.addNode(request);
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/remove_node")
    @ApiOperation(value = REMOVE_NODE_FROM_CLUSTER, nickname = "removeNode")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> removeNode(
            @ApiParam(required = true, value = REMOVE_CLUSTER_NODE_REQUEST_NOTES) @RequestBody RemoveClusterNodeRequest request
    ) {
        return service.removeNode(request);
    }

}
