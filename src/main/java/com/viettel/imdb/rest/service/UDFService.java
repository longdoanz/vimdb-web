package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.model.EditUDFRequest;
import com.viettel.imdb.rest.model.InsertUDFRequest;
import com.viettel.imdb.rest.model.UDFInfo;

import java.util.List;

public interface UDFService {
    List<UDFInfo> getUDFs();
    UDFInfo getUdfByName(String udfName);
    void insertUDF(String udfName, InsertUDFRequest request);
    void updateUDF(String udfName, EditUDFRequest request);
    void delete(String udfName);
}
