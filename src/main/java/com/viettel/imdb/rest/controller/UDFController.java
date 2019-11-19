package com.viettel.imdb.rest.controller;

import com.viettel.imdb.rest.model.EditUDFRequest;
import com.viettel.imdb.rest.model.InsertUDFRequest;
import com.viettel.imdb.rest.model.UDFInfo;
import com.viettel.imdb.rest.service.UDFService;
import io.swagger.annotations.*;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@Api(tags = "UDF related operations", description = "Perform operations related to UDF")
@RestController
@RequestMapping("/v1/udf")
public class UDFController {
    private static final String GET_UDFs_NOTES = " ";
    private static final String GET_UDF_BY_NAME_NOTES = "GET UDF BY NAME";
    private static final String ADD_UDF = "  ";
    private static final String ADD_UDF_NOTES = "  ";
    private static final String UPDATE_UDF_NOTES = "  ";
    private static final String UDF_NOTES = "  ";
    private static final String UPDATE_UDF_REQUEST_NOTES = "  ";
    private static final String DROP_UDF_NOTES = "";

    @Autowired private UDFService service;

    @RequestMapping(method = RequestMethod.GET, value = "")
    @ApiOperation(value = GET_UDFs_NOTES, nickname = "getUDFs")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    response = UDFInfo.class,
                    message = "OK",
                    responseContainer = "List"
            )
    })
    public List<UDFInfo> getUDFs() {
        return service.getUDFs();
    }


    @GetMapping(value = "/{udfName}")
    @ApiOperation(value = GET_UDF_BY_NAME_NOTES, nickname = "getUDFByName")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    response = UDFInfo.class,
                    message = "OK"
            )
    })
    public UDFInfo getUdfByName(@PathVariable String udfName) {
        Logger.info("Get UDF ({})", udfName);
        return service.getUdfByName(udfName);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/{udfName}")
    @ApiOperation(value = ADD_UDF, nickname = "addUDF")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertUDF(
            @ApiParam(required = true, value = ADD_UDF_NOTES) @PathVariable String udfName,
            @ApiParam(required = true, value = ADD_UDF_NOTES) @RequestBody InsertUDFRequest request
    ) {
        service.insertUDF(udfName, request);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{udfName}")
    @ApiOperation(value = UPDATE_UDF_NOTES, nickname = "updateFieldList")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(
            @ApiParam(required = true, value = UDF_NOTES) @PathVariable(value = "udfName") String udfName,
            @ApiParam(required = true, value = UPDATE_UDF_REQUEST_NOTES) @RequestBody EditUDFRequest request,
            @ApiIgnore @RequestParam Map<String, String> requestParams // additional input - no need by now
    ) {
        Logger.info("update UDF({})", udfName);
        service.updateUDF(udfName, request);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{udfName}")
    @ApiOperation(value=DROP_UDF_NOTES, nickname = "dropNamespace")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)

    public void dropUDF(
            @ApiParam(required = true, value = UDF_NOTES) @PathVariable(value = "udfName") String udfName
    ) {
        Logger.info("Drop UDF({}, {})", udfName);
        service.delete(udfName);
    }
}
