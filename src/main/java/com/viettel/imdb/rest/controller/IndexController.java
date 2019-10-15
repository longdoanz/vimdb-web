//package com.viettel.imdb.rest.controller;
//
//import com.viettel.imdb.rest.RestErrorCode;
//import com.viettel.imdb.rest.common.ResponseHandler;
//import com.viettel.imdb.rest.model.IndexModel;
//import com.viettel.imdb.rest.model.PrivilegeType;
//import com.viettel.imdb.rest.model.TableModel;
//import com.viettel.imdb.rest.service.IndexService;
//import com.viettel.imdb.rest.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.context.request.async.DeferredResult;
//
//@RestController
//public class IndexController {
//
//    @Autowired
//    IndexService indexService;
//
//    @Autowired
//    private UserService userService;
//
//    //    Index API
//    @PostMapping("/{db}/{tableName}/indexes")
//    public DeferredResult<ResponseEntity> createIndex(@PathVariable("db") String db,
//                                                      @PathVariable("tableName") String tableName,
//                                                      @RequestBody IndexModel indexModel) {
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
//
//        TableModel requestTable = new TableModel(db, tableName);
//        Enum errorCode = requestTable.validateData();
//
//        if (errorCode == RestErrorCode.OK) {
//
//            if (!userService.checkAccessRight(PrivilegeType.DATA_ADMIN, null, null)) {
//                deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//                return deferred;
//            }
//
//            indexService.createIndex(requestTable, indexModel).onSuccess(result -> {
//                ResponseHandler.generateResponse(deferred, result);
//            }).onFailure(throwable -> {
//                ResponseHandler.generateInternalErrorResponse(deferred, throwable.getMessage());
//            });
//        } else {
//            ResponseHandler.generateValidatorError(deferred, errorCode);
//        }
//
//        return deferred;
//    }
//
//    //    DROP INDEX
//    @DeleteMapping("/{db}/{tableName}/indexes/{index}")
//    public DeferredResult<ResponseEntity> dropIndex(@PathVariable("db") String db,
//                                                    @PathVariable("tableName") String tableName,
//                                                    @PathVariable("index") String indexField) {
//
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
//        TableModel requestTable = new TableModel(db, tableName);
//        Enum errorCode = requestTable.validateData();
//
//        if (errorCode == RestErrorCode.OK) {
//
//            if (!userService.checkAccessRight(PrivilegeType.DATA_ADMIN, null, null)) {
//                deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//                return deferred;
//            }
//
//            indexService.dropIndex(requestTable, new IndexModel(indexField).getName()).onSuccess(result -> {
//                ResponseHandler.generateResponse(deferred, result);
//            }).onFailure(throwable -> {
//                ResponseHandler.generateInternalErrorResponse(deferred, throwable.getMessage());
//            });
//        } else {
//            ResponseHandler.generateValidatorError(deferred, errorCode);
//        }
//
//
//        return deferred;
//    }
//
//}
