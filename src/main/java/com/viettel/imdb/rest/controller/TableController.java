//package com.viettel.imdb.rest.controller;
//
//import com.viettel.imdb.rest.exception.RestErrorCode;
//import com.viettel.imdb.rest.common.ResponseHandler;
//import com.viettel.imdb.rest.model.PrivilegeType;
//import com.viettel.imdb.rest.model.TableModel;
//import com.viettel.imdb.rest.service.TableService;
//import com.viettel.imdb.rest.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.context.request.async.DeferredResult;
//
///**
// * @author longdt20
// * @since 05/12/2018.
// */
//
//@RestController
//public class TableController {
//
//    private final TableService tableService;
//
//    private UserService userService;
//
//    @Autowired
//    public TableController(TableService tableService, UserService userService) {
//        this.tableService = tableService;
//        this.userService = userService;
//    }
//
//    /*public static DeferredResult<ResponseEntity> getTableList(String db) {
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
//        return deferred;
//    }*/
//
//    @PostMapping("/{db}")
//    public DeferredResult<ResponseEntity> createTable(
//            @PathVariable("db") String db,
//            @RequestBody TableModel requestTable
//    ) {
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
////            Validate database and table name
//        requestTable.setDb(db);
//        Enum errorCode = requestTable.validateData();
//
//        if (errorCode == RestErrorCode.OK) {
//            if (!userService.checkAccessRight(PrivilegeType.DATA_ADMIN, null, null)) {
//                deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//                return deferred;
//            }
//
//            tableService.createTable(requestTable).onSuccess((result -> {
//                deferred.setResult((new ResponseEntity<>(result.getResponse(), result.getHttpStatus())));
//            })).onFailure(throwable -> ResponseHandler.generateInternalErrorResponse(deferred, throwable.getMessage()));
//
//        } else {
//            ResponseHandler.generateValidatorError(deferred, errorCode);
//        }
//
//        return deferred;
//    }
//
//    //    @PreAuthorize("hasRole('USER')")
//    @DeleteMapping("/{db}/{tableName}")
//    public DeferredResult<ResponseEntity> dropTable(
//            @PathVariable("db") String db,
//            @PathVariable("tableName") String tableName) {
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
//
//        TableModel requestTable = new TableModel(db, tableName);
//        Enum errorCode = requestTable.validateData();
//
//        if (errorCode == RestErrorCode.OK) {
//            if (!userService.checkAccessRight(PrivilegeType.DATA_ADMIN, null, null)) {
//                deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//                return deferred;
//            }
//
//            tableService.dropTable(requestTable).onSuccess(result -> {
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
//}
