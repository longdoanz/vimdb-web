package com.viettel.imdb.rest.controller;

import com.viettel.imdb.common.Field;
import com.viettel.imdb.rest.domain.RestClientError;
import com.viettel.imdb.rest.model.EditUDFRequest;
import com.viettel.imdb.rest.model.InsertUDFRequest;
import com.viettel.imdb.rest.model.UDFInfo;
import com.viettel.imdb.rest.model.UDFRespone;
import com.viettel.imdb.rest.service.UDFService;
import io.swagger.annotations.*;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@Api(tags = "UDF related operations", description = "Perform operations related to UDF")
@RestController
@RequestMapping("/v1/udf")
public class UDFController {
    private static final String GET_UDFs_NOTES = " ";
    private static final String ADD_UDF = "  ";
    private static final String ADD_UDF_NOTES = "  ";
    private static final String UPDATE_UDF_NOTES = "  ";
    private static final String UDF_NOTES = "  ";
    private static final String UPDATE_UDF_REQUEST_NOTES = "  ";
    private static final String DROP_UDF_NOTES = "";

    @Autowired private UDFService service;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ApiOperation(value = GET_UDFs_NOTES, nickname = "getUDFs")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    response = UDFInfo.class,
                    message = "OK",
                    responseContainer = "List"

                    //examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            ),
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> getUDFs() {
        return service.getUDFs();
    }


    @RequestMapping(method = RequestMethod.POST, value = "/{udf_name}")
    @ApiOperation(value = ADD_UDF, nickname = "addUDF")
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
    public DeferredResult<ResponseEntity<?>> insertUDF(
            @ApiParam(required = true, value = ADD_UDF_NOTES) @PathVariable String udf_name,
            @ApiParam(required = true, value = ADD_UDF_NOTES) @RequestBody InsertUDFRequest request
    ) {
        request.setFileName(udf_name+"."+request.getType());
        return service.insertUDF(request);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{udf_name}")
    @ApiOperation(value = UPDATE_UDF_NOTES, nickname = "updateFieldList")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> update(
            @ApiParam(required = true, value = UDF_NOTES) @PathVariable(value = "udf_name") String udf_name,
            @ApiParam(required = true, value = UPDATE_UDF_REQUEST_NOTES) @RequestBody EditUDFRequest request,
            @ApiIgnore @RequestParam Map<String, String> requestParams // additional input - no need by now
    ) {
        Logger.error("update({})", udf_name);
        return service.updateUDF(udf_name, request);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{udf_name}")
    @ApiOperation(value=DROP_UDF_NOTES, nickname = "dropNamespace")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 666,
                    response = RestClientError.class,
                    message = "Drop not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> dropUDF(
            @ApiParam(required = true, value = UDF_NOTES) @PathVariable(value = "udf_name") String udf_name
    ) {
        Logger.error("Drop udf({}, {})", udf_name);
        return service.delete(udf_name);
    }
}
