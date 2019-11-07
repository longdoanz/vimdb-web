package com.viettel.imdb.rest.controller;

import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {
    /*private static final String CONFIGURATIONS = "CONFIGURATION";
    private static final String CLUSTER = "CLUSTER";

    @Autowired
    private TableController tableController;

    @GetMapping("/{path}")
    public DeferredResult<ResponseEntity> rootGetHandler(@PathVariable("path") String path) {
        switch (path) {
            case CONFIGURATIONS:
            case CLUSTER:
            default:
                return tableController.getTableList(path);
        }
    }

    @PostMapping("/{path}")
//    public ResponseEntity rootPostHandler(@PathVariable("path") String path, @RequestBody JsonNode requestBody) {
    public DeferredResult<ResponseEntity> rootPostHandler(@PathVariable("path") String path, @RequestBody TableModel TableModel) {
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        switch (path) {
            case CONFIGURATIONS:
            case CLUSTER:
            default:
                return new tableController.createTable(path, TableModel.tableName);
        }
    }

    @PutMapping("/{path}")
    public DeferredResult<ResponseEntity> rootPutHandler(@PathVariable("path") String path) {
        switch (path) {
            case CONFIGURATIONS:
            case CLUSTER:
            default:
                return tableController.create;
        }
    }

    @DeleteMapping("/{path}")
    public DeferredResult<ResponseEntity> rootDeleteHandler(@PathVariable("path") String path) {
        switch (path) {
            case CONFIGURATIONS:
            case CLUSTER:
            default:
                return TableController.getTableList(path);
        }
    }*/
}
