package com.viettel.imdb.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.viettel.imdb.rest.common.Utils;
import com.viettel.imdb.rest.domain.RestClientError;
import com.viettel.imdb.rest.domain.RestIndexModel;
import com.viettel.imdb.rest.domain.RestScanModel;
import com.viettel.imdb.rest.model.TableModel;
import com.viettel.imdb.rest.service.DataService;
import com.viettel.imdb.rest.service.StatisticService;
import com.viettel.imdb.rest.util.RequestParamHandler;
import io.swagger.annotations.*;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author quannh22
 * @since 08/08/2019
 */
@Api(tags = "Data related operations", description = "Perform operations related to data")
@RestController
@RequestMapping("/v1/data")
public class DataController {
    /**
     ************************************************
     *             CONSTANTS COME HERE              *
     ************************************************
     */
    private static final String CREATE_NAMESPACE_NOTES = "Create a namespace with input namespace name";
    private static final String DROP_NAMESPACE_NOTES = "Drop a namespace with input namespace name";
    private static final String UPDATE_NAMESPACE_NOTES = "Update a namespace with inpuinsertt namespace name";
    private static final String CREATE_TABLE_NOTES = "Create a table with input namespace and table name";
    private static final String DROP_TABLE_NOTES = "Drop the given table with input namespace and table name";
    private static final String CREATE_INDEX_NOTES = "Create a secondary index inside a table";
    private static final String DROP_INDEX_NOTES = "Drop a secondary index from a table";
    private static final String SELECT_NOTES = "Select a record";
    private static final String INSERT_FIELD_LIST_NOTES = "Insert a record with input field list";
    private static final String INSERT_JSON_NOTES = "Insert a record with input json";
    private static final String UPDATE_FIELD_LIST_NOTES = "Update a record with input field list";
    private static final String UPDATE_JSON_NOTES = "Update a record with input json";
    private static final String UPSERT_FIELD_LIST_NOTES = "Upsert a record with input field list";
    private static final String UPSERT_JSON_NOTES = "Upsert a record with input json";
    private static final String REPLACE_FIELD_LIST_NOTES = "Replace a record with input field list";
    private static final String REPLACE_JSON_NOTES = "Replace a record with input json";
    private static final String SCAN_NOTES = "Scan records in a table";
    private static final String SCAN_NAMESPACE_NOTES = "Scan namespace and table";

    private static final String NAMESPACE_NOTES = "Namespace contains tables, equals to db in relational database";
    private static final String TABLE_NOTES = "Table contains records";
    private static final String INDEX_NAME_NOTES = "Secondary index inside a table";
    private static final String KEY_NOTES = "Primary key of a record";
    private static final String FIELDNAME_LIST_NOTES = "List of fieldnames. Valid format depends on API";
    private static final String FIELD_LIST_NOTES = "List of fields. Each field contains a field name and its value";
    private static final String JSON_NOTES = "Input json value";
    private static final String INDEX_MODEL_NOTES = "Index model, contains index name and type";
    private static final String SCAN_MODEL_NOTES = "Scan model contains secondary index name, type, min and max values";
    private static final String DELETE_NOTES = "Delete a key or some fields of a key";


    /**
     * Data Service to mainly serve request from this controller
     */
    @Autowired private DataService service;

    @RequestMapping(method = RequestMethod.GET, value = "")
    @ApiOperation(value=SCAN_NAMESPACE_NOTES, nickname = "scan")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public ResponseEntity<?> getDataInfo(
    ) {
        return service.getDataInfo();
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    @ApiOperation(value=CREATE_NAMESPACE_NOTES, nickname = "createNamespace")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 444,
                    response = RestClientError.class,
                    message = "Table exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> createNamespace(
            @ApiParam(required = false, value = NAMESPACE_NOTES, example = "{'name': 'NS_01' }") @RequestBody(required = true) JsonNode body
    ) {
        System.out.println(body);
        String namespace = "";
        Logger.info("Create namespace({})", namespace);
        return service.createNamespace(namespace);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{namespace}")
    @ApiOperation(value=DROP_NAMESPACE_NOTES, nickname = "dropNamespace")
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
    public DeferredResult<ResponseEntity<?>> dropNamespace(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace
    ) {
        Logger.error("Drop namespace({}, {})", namespace);
        return service.dropNamespace(namespace);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{namespace}")
    @ApiOperation(value = UPDATE_NAMESPACE_NOTES, nickname = "updateNamespace")
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
    public DeferredResult<ResponseEntity<?>> updateNamespace(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = NAMESPACE_NOTES) @RequestBody String newname,
            @ApiIgnore @RequestParam Map<String, String> requestParams // additional input - no need by now
    ) {
        Logger.error("update(old name{}  new name )", namespace, newname);
        return service.updateNamespace(namespace, newname);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/{namespace}")
    @ApiOperation(value=CREATE_TABLE_NOTES, nickname = "createTable")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 444,
                    response = RestClientError.class,
                    message = "Table exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> createTable(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value="namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @RequestBody TableModel tableName
    ) {
        Logger.error("Create table({}, {})", namespace, tableName);
        return service.createTable(namespace, tableName.getTableName());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{namespace}/{tablename}")
    @ApiOperation(value=DROP_TABLE_NOTES, nickname = "dropTable")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 666,
                    response = RestClientError.class,
                    message = "Table not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> dropTable(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName
    ) {
        Logger.error("Drop table({}, {})", namespace, tableName);
        return service.dropTable(namespace, tableName);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{namespace}/{tablename}")
    @ApiOperation(value = CREATE_INDEX_NOTES, nickname = "createIndex")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 555,
                    response = RestClientError.class,
                    message = "Index exists",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> createIndex(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName,
            @ApiParam(required = true, value = INDEX_MODEL_NOTES) @RequestBody RestIndexModel indexModel

    ) {
        Logger.error("Create Index({}, {}, {})", namespace, tableName, indexModel);
        indexModel.setNamespace(namespace);
        indexModel.setTable(tableName);
        return service.createIndex(indexModel);
    }

    // TODO COMMENT OUT DROP INDEX NEED REVERSE
/*
    @RequestMapping(method = RequestMethod.DELETE, value = "/{namespace}/{tablename}/{indexname}")
    @ApiOperation(value = DROP_INDEX_NOTES, nickname = "dropIndex")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 555,
                    response = RestClientError.class,
                    message = "Index does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> dropIndex(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName,
            @ApiParam(required = true, value = INDEX_NAME_NOTES) @PathVariable(value = "indexname") String indexName
    ) {
        Logger.error("Drop Index({}, {}, {})", namespace, tableName, indexName);
        return service.dropIndex(namespace, tableName, indexName);
    }
*/

    @RequestMapping(method = RequestMethod.GET, value = "/{namespace}/{tablename}/{key}")
    @ApiOperation(value = SELECT_NOTES, nickname = "select")
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
    public DeferredResult<ResponseEntity<?>> select(
        @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
        @ApiParam(required = true, value = TABLE_NOTES) @PathVariable(value = "tablename") String tableName,
        @ApiParam(required = true, value = KEY_NOTES) @PathVariable(value = "key") String key,
        @ApiIgnore(value = FIELDNAME_LIST_NOTES) @RequestParam MultiValueMap<String, String> requestParams
    ) {
        List<String> fieldNameList = RequestParamHandler.getFieldNameListFromMap(requestParams);
        Logger.error("Select({}, {}, {})", tableName, key, fieldNameList);
        return service.select(namespace, tableName, key, fieldNameList);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{namespace}/{tablename}/{key}")
    @ApiOperation(value = INSERT_FIELD_LIST_NOTES, nickname = "insertFieldList")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key exists",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> insert(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName,
            @ApiParam(required = true, value = KEY_NOTES) @PathVariable("key") String key,
            @ApiParam(required = true, value = FIELD_LIST_NOTES) @RequestBody JsonNode jsonNode/*,
            @ApiParam(required = true, value = FIELD_LIST_NOTES) @RequestBody List<Field> fieldList*/
    ) {
        System.out.println(jsonNode);
        Logger.error("insert({}, {}, {}, {})", namespace, tableName, key);
        return service.insert(namespace, tableName, key, Utils.getFieldValue(jsonNode));
    }

    /*
    @RequestMapping(method = RequestMethod.POST, value = "/{namespace}/{tablename}/{key}")
    @ApiOperation(value = INSERT_JSON_NOTES, nickname = "insertJson")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key exists",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> insert(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName,
            @ApiParam(required = true, value = KEY_NOTES) @PathVariable("key") String key,
            @ApiParam(required = true, value = JSON_NOTES) @RequestBody String json,
            @ApiIgnore @RequestParam Map<String, String> requestParams // additional input - no need by now
    ) {
        return service.insert(namespace, tableName, key, json);
    }
    */

    @RequestMapping(method = RequestMethod.PATCH, value = "/{namespace}/{tablename}/{key}")
    @ApiOperation(value = UPDATE_FIELD_LIST_NOTES, nickname = "updateFieldList")
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
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName,
            @ApiParam(required = true, value = KEY_NOTES) @PathVariable("key") String key,
            @ApiParam(required = true, value = FIELD_LIST_NOTES) @RequestBody JsonNode jsonNode,
            @ApiIgnore @RequestParam Map<String, String> requestParams // additional input - no need by now
    ) {
        Logger.error("update({}, {}, {}, {})", namespace, tableName, key);
        return service.update(namespace, tableName, key, Utils.getFieldValue(jsonNode));
    }
    /*

    @RequestMapping(method = RequestMethod.PATCH, value = "/{namespace}/{tablename}/{key}")
    @ApiOperation(value = UPDATE_JSON_NOTES, nickname = "updateJson")
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
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName,
            @ApiParam(required = true, value = KEY_NOTES) @PathVariable("key") String key,
            @ApiParam(required = true, value = JSON_NOTES) @RequestBody String json,
            @ApiIgnore @RequestParam Map<String, String> requestParams  // additional input - no need by now
    ) {
        return service.update(namespace, tableName, key, json);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{namespace}/{tablename}/{key}")
    @ApiOperation(value = UPSERT_FIELD_LIST_NOTES, nickname = "upsertFieldList")
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
    public DeferredResult<ResponseEntity<?>> upsert(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName,
            @ApiParam(required = true, value = KEY_NOTES) @PathVariable("key") String key,
            @ApiParam(required = true, value = FIELD_LIST_NOTES) @RequestBody List<Field> fieldList,
            @ApiIgnore @RequestParam Map<String, String> requestParams  // additional input - no need by now
    ) {
        return service.upsert(namespace, tableName, key, fieldList);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{namespace}/{tablename}/{key}")
    @ApiOperation(value = UPSERT_JSON_NOTES, nickname = "upsertJson")
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
    public DeferredResult<ResponseEntity<?>> upsert(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName,
            @ApiParam(required = true, value = KEY_NOTES) @PathVariable("key") String key,
            @ApiParam(required = true, value = JSON_NOTES) @RequestBody String json,
            @ApiIgnore @RequestParam Map<String, String> requestParams  // additional input - no need by now
    ) {
        return service.upsert(namespace, tableName, key, json);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{namespace}/{tablename}/{key}")
    @ApiOperation(value = REPLACE_FIELD_LIST_NOTES, nickname = "replaceFieldList")
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
    public DeferredResult<ResponseEntity<?>> replace(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName,
            @ApiParam(required = true, value = KEY_NOTES) @PathVariable("key") String key,
            @ApiParam(required = true, value = FIELD_LIST_NOTES) @RequestBody List<Field> fieldList,
            @ApiIgnore @RequestParam Map<String, String> requestParams  // additional input - no need by now
    ) {
        return service.replace(namespace, tableName, key, fieldList);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{namespace}/{tablename}/{key}")
    @ApiOperation(value = REPLACE_JSON_NOTES, nickname = "replaceJson")
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
    public DeferredResult<ResponseEntity<?>> replace(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName,
            @ApiParam(required = true, value = KEY_NOTES) @PathVariable("key") String key,
            @ApiParam(required = true, value = JSON_NOTES) @RequestBody String json,
            @ApiIgnore @RequestParam Map<String, String> requestParams  // additional input - no need by now
    ) {
        return service.replace(namespace, tableName, key, json);
    }
    */

    @RequestMapping(method = RequestMethod.DELETE, value = "/{namespace}/{tablename}/{key}")
    @ApiOperation(value = DELETE_NOTES, nickname = "delete")
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
    @ApiIgnore
    public DeferredResult<ResponseEntity<?>> delete(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName,
            @ApiParam(required = true, value = KEY_NOTES) @PathVariable("key") String key,
            @ApiIgnore(value = FIELDNAME_LIST_NOTES) @RequestParam MultiValueMap<String, String> requestParams
    ) {
        List<String> fieldNameList = RequestParamHandler.getFieldNameListFromMap(requestParams);
        Logger.error("delete({}, {}, {}, {})", namespace, tableName, key, fieldNameList);
        return service.delete(namespace, tableName, key, fieldNameList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{namespace}/{tablename}")
    @ApiOperation(value = SCAN_NOTES, nickname = "scan")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 777,
                    response = RestClientError.class,
                    message = "Key does not exist",
                    examples = @Example(value={@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' }")})
            )
            // other @ApiResponses
    })
    public DeferredResult<ResponseEntity<?>> scan(
            @ApiParam(required = true, value = NAMESPACE_NOTES) @PathVariable(value = "namespace") String namespace,
            @ApiParam(required = true, value = TABLE_NOTES) @PathVariable("tablename") String tableName,
            @ApiParam(required = true, value = SCAN_MODEL_NOTES) @RequestBody RestScanModel restScanModel,
            @ApiIgnore @RequestParam Map<String, String> requestParams
    ) {
        restScanModel.setNamespace(namespace);
        restScanModel.setTable(tableName);
        Logger.error("scan({}, {}, {}", namespace, tableName, restScanModel);
        return service.scan(namespace, tableName, restScanModel);
    }


}
