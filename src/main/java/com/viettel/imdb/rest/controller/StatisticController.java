package com.viettel.imdb.rest.controller;

import com.viettel.imdb.rest.domain.RestClientError;
import com.viettel.imdb.rest.model.StatisticFilter;
import com.viettel.imdb.rest.service.StatisticService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author quannh22
 * @since 08/08/2019
 */
@Api(tags = "Statistic related operations", description = "Perform operations related to Statistic")
@RestController
@RequestMapping("/v1/statistic")
public class StatisticController {
    private static final String GET_METRICS_NOTES = "";
    private static final String SERVER_NOTES = " ";
    @Autowired
    private StatisticService service;

    @RequestMapping(method = RequestMethod.GET, value = "/metrics")
    @ApiOperation(value = GET_METRICS_NOTES, nickname = "getMetrics")
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
    public DeferredResult<ResponseEntity<?>> getMetrics(
            @ApiParam(required = false, value = SERVER_NOTES) @PathVariable(value = "metrics") StatisticFilter param
            ) {
        return service.getMetrics(param);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ApiOperation(value = GET_METRICS_NOTES, nickname = "getStatistic")
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
    public DeferredResult<ResponseEntity<?>> getStatistic(
            @ApiParam(required = false, value = SERVER_NOTES) @PathVariable(value = "metrics") StatisticFilter param
    ) {
        return service.getStatistics(param);
    }
}
