package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.model.EditUDFRequest;
import com.viettel.imdb.rest.model.InsertUDFRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface UDFService {
    DeferredResult<ResponseEntity<?>> getUDFs();
    DeferredResult<ResponseEntity<?>> insertUDF(String udf_name, InsertUDFRequest request);
    DeferredResult<ResponseEntity<?>> updateUDF(String udf_name, EditUDFRequest request);
    DeferredResult<ResponseEntity<?>> delete(String udf_name);
}
