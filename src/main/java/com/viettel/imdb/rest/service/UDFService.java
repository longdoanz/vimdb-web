package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.model.EditUDFRequest;
import com.viettel.imdb.rest.model.InsertUDFRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface UDFService {
    DeferredResult<ResponseEntity<?>> getUDFs();
    DeferredResult<?> getUdfByName(String udfName);
    DeferredResult<ResponseEntity<?>> insertUDF(String udfName, InsertUDFRequest request);
    void updateUDF(String udfName, EditUDFRequest request);
    void delete(String udfName);
}
