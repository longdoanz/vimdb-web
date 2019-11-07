//package com.viettel.imdb.rest.controller;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.viettel.imdb.rest.common.ResponseHandler;
//import com.viettel.imdb.rest.model.PrivilegeType;
//import com.viettel.imdb.rest.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.context.request.async.DeferredResult;
//
//@RestController
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping(value = "/user/{username}")
//    public DeferredResult<ResponseEntity> getUser(@PathVariable("username") String username) {
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
//
//        if (!userService.checkAccessRight(PrivilegeType.USER_ADMIN, null, null)) {
//            deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//            return deferred;
//        }
//
//        userService.getUser(username).onSuccess(result -> {
//            ResponseHandler.generateResponse(deferred, result);
//        }).onFailure(throwable -> {
//            ResponseHandler.generateInternalErrorResponse(deferred);
//        });
//
//        return deferred;
//    }
//
//    @PostMapping(value = "/user")
//    public DeferredResult<ResponseEntity> createUser(@RequestBody JsonNode jsonNode) {
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
//
//        if (!userService.checkAccessRight(PrivilegeType.USER_ADMIN, null, null)) {
//            deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//            return deferred;
//        }
//
//        userService.insertUser(jsonNode).onSuccess(result -> {
//            ResponseHandler.generateResponse(deferred, result);
//        }).onFailure(throwable -> {
//            ResponseHandler.generateInternalErrorResponse(deferred);
//        });
//
//        return deferred;
//    }
//
//    @PostMapping(value = "/user/change-password")
//    public DeferredResult<ResponseEntity> changePassword(@RequestParam("username") String username, @RequestParam("password") String password) {
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
//
//        if (!userService.checkAccessRight(PrivilegeType.USER_ADMIN, null, null)) {
//            deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//            return deferred;
//        }
//
//        userService.changePassword(username, password).onSuccess(result -> {
//            ResponseHandler.generateResponse(deferred, result);
//        }).onFailure(throwable -> {
//            ResponseHandler.generateInternalErrorResponse(deferred);
//        });
//
//        return deferred;
//    }
//
//    @DeleteMapping(value = "/user/{username}")
//    public DeferredResult<ResponseEntity> createUser(@PathVariable("username") String username) {
//        DeferredResult<ResponseEntity> deferred = new DeferredResult<>();
//
//        if (!userService.checkAccessRight(PrivilegeType.USER_ADMIN, null, null)) {
//            deferred.setResult(new ResponseEntity<>(HttpStatus.FORBIDDEN));
//            return deferred;
//        }
//
//        userService.deleteUser(username).onSuccess(result -> {
//            ResponseHandler.generateResponse(deferred, result);
//        }).onFailure(throwable -> {
//            ResponseHandler.generateInternalErrorResponse(deferred);
//        });
//
//        return deferred;
//    }
//
//}
