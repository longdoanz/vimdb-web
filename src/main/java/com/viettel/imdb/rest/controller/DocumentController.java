//package com.viettel.imdb.rest.controller;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.viettel.imdb.rest.RestErrorCode;
//import com.viettel.imdb.rest.common.ResponseHandler;
//import com.viettel.imdb.rest.model.FilterModel;
//import com.viettel.imdb.rest.model.PrivilegeType;
//import com.viettel.imdb.rest.model.TableModel;
//import com.viettel.imdb.rest.service.DocumentService;
//import com.viettel.imdb.rest.service.UserService;
//import net.openhft.chronicle.core.annotation.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.context.request.async.DeferredResult;
//
//
///**
// * @author longdt20
// * @since 14:37 05/12/2018
// */
//
//@RestController
//public class DocumentController {
//
//    @Autowired
//    private DocumentService documentService;
//
//    @Autowired
//    private UserService userService;
//
//
//    @GetMapping(value = "/{db}/{tableName}")
//    public DeferredResult<ResponseEntity> getSecondaryIndexRecord(@PathVariable("db") String db,
//                                                                  @PathVariable("tableName") String tableName,
//                                                                  @NotNull @RequestParam(value = "filter") String filter,
//                                                                  @RequestParam(value = "fields", defaultValue = "") String fields) {
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
//
//        //            Validate database and table name
//        TableModel requestTable = new TableModel(db, tableName);
//        Enum errorCode = requestTable.validateData();
//
//        if (errorCode == RestErrorCode.OK) {
//            if (!userService.checkAccessRight(PrivilegeType.READ, db, tableName)) {
//                deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//                return deferred;
//            }
//
////            Validate filter data
//            FilterModel filterModel = new FilterModel();
//            errorCode = filterModel.setData(filter);
//
//            if (errorCode == RestErrorCode.OK) {
//                documentService.scan(db, tableName, filterModel, fields).onSuccess(result -> {
//                    deferred.setResult((new ResponseEntity<>(result.getResponse(), result.getHttpStatus())));
//                }).onFailure(throwable -> {
//                    throwable.printStackTrace();
//                    ResponseHandler.generateInternalErrorResponse(deferred, throwable.getMessage());
//                });
//            } else {
//                ResponseHandler.generateValidatorError(deferred, errorCode);
//            }
//        } else {
//            ResponseHandler.generateValidatorError(deferred, errorCode);
//        }
//
//        return deferred;
//    }
//
//    @GetMapping(value = "/{db}/{tableName}/{key}")
//    public DeferredResult<ResponseEntity> getRecord(@PathVariable("db") String db,
//                                                    @PathVariable("tableName") String tableName,
//                                                    @PathVariable("key") String key,
//                                                    @RequestParam(value = "fields", defaultValue = "") String fields) {
//
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
//
////            Validate database and table name
//        TableModel requestTable = new TableModel(db, tableName);
//        Enum errorCode = requestTable.validateData();
//
//        if (errorCode == RestErrorCode.OK) {
//            if (!userService.checkAccessRight(PrivilegeType.READ, db, tableName)) {
//                deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//                return deferred;
//            }
//
//            documentService.select(db, tableName, key, fields).onSuccess(result -> {
//
//                Object data = result.getData();
//                deferred.setResult(new ResponseEntity<>((data == null) ? result.getResponse() : data, result.getHttpStatus()));
//
//            }).onFailure(throwable -> {
//                throwable.printStackTrace();
//                ResponseHandler.generateInternalErrorResponse(deferred, throwable.getMessage());
//            });
//        } else {
//            ResponseHandler.generateValidatorError(deferred, errorCode);
//            return deferred;
//        }
//
//
//        return deferred;
//    }
//
//
//    @PostMapping(value = "/{db}/{tableName}")
//    public DeferredResult<ResponseEntity> insertRecord(
//            @PathVariable("db") String db,
//            @PathVariable("tableName")
//            String tableName,
//            @RequestBody JsonNode jsonNode) {
//
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
//
//        TableModel requestTable = new TableModel(db, tableName);
//        Enum errorCode = requestTable.validateData();
//
//        if (errorCode == RestErrorCode.OK) {
//            if (!userService.checkAccessRight(PrivilegeType.READ_WRITE, db, tableName)) {
//                deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//                return deferred;
//            }
//
//            documentService.insert(db, tableName, jsonNode).onSuccess(result -> {
//                deferred.setResult((new ResponseEntity<>(result.getResponse(), result.getHttpStatus())));
//            }).onFailure(throwable -> {
//                throwable.printStackTrace();
//                ResponseHandler.generateInternalErrorResponse(deferred, throwable.getMessage());
//            });
//        } else {
//            ResponseHandler.generateValidatorError(deferred, errorCode);
//        }
//
//        return deferred;
//    }
//
//    @PutMapping(value = "/{db}/{tableName}/{key}")
//    public DeferredResult<ResponseEntity> updateRecord(@PathVariable("db") String db, @PathVariable("tableName")
//            String tableName, @PathVariable("key") String key, @RequestBody JsonNode jsonNode) {
//
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
//
////            Validate database and table name
//        TableModel requestTable = new TableModel(db, tableName);
//        Enum errorCode = requestTable.validateData();
//
//        if (errorCode == RestErrorCode.OK) {
//            if (!userService.checkAccessRight(PrivilegeType.READ_WRITE, db, tableName)) {
//                deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//                return deferred;
//            }
//
//            documentService.update(db, tableName, key, jsonNode).onSuccess(result -> {
//                deferred.setResult((new ResponseEntity<>(result.getResponse(), result.getHttpStatus())));
//            }).onFailure(throwable -> {
//                throwable.printStackTrace();
//                ResponseHandler.generateInternalErrorResponse(deferred, throwable.getMessage());
//            });
//        } else {
//            ResponseHandler.generateValidatorError(deferred, errorCode);
//        }
//
//        return deferred;
//    }
//
//    @DeleteMapping(value = "/{db}/{tableName}/{key}")
//    public DeferredResult<ResponseEntity> deleteRecord(@PathVariable("db") String db, @PathVariable("tableName")
//            String tableName, @PathVariable("key") String key) {
//
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
////            Validate database and table name
//        TableModel requestTable = new TableModel(db, tableName);
//        Enum errorCode = requestTable.validateData();
//
//        if (errorCode == RestErrorCode.OK) {
//            if (!userService.checkAccessRight(PrivilegeType.READ_WRITE, db, tableName)) {
//                deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//                return deferred;
//            }
//
//            documentService.delete(db, tableName, key).onSuccess(result -> {
//                deferred.setResult((new ResponseEntity<>(result.getResponse(), result.getHttpStatus())));
//            }).onFailure(throwable -> {
//                throwable.printStackTrace();
//                ResponseHandler.generateInternalErrorResponse(deferred, throwable.getMessage());
//            });
//        } else {
//            ResponseHandler.generateValidatorError(deferred, errorCode);
//        }
//
//        return deferred;
//    }
//}
