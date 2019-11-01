package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.exception.ExceptionType;
import com.viettel.imdb.rest.model.EditUDFRequest;
import com.viettel.imdb.rest.model.InsertUDFRequest;
import com.viettel.imdb.rest.model.UDFInfo;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@Service
public class UDFServiceImpl implements UDFService{

    List<UDFInfo> udfList;

    @Autowired
    public UDFServiceImpl(List<UDFInfo> udfInfoList) {
        this.udfList = udfInfoList;
    }


    @Override
    public DeferredResult<ResponseEntity<?>> getUDFs() {
        Logger.info("getUDFs(     )");
//        for (int i = 0; i < 10; i++){
//            UDFInfo UDF = new UDFInfo();
//            UDFList.add(UDF);
//        }
        DeferredResult res = new DeferredResult<>();
        res.setResult(udfList);
        return res;
    }

    @Override
    public DeferredResult<?> getUdfByName(String udfName) {
        System.err.println("=-----------------------------------------");
        DeferredResult<UDFInfo> future = new DeferredResult<>();
        for(UDFInfo udf : udfList) {
            if(udf.getName().equals(udfName)) {
                future.setResult(udf);
                return future;
            }
        }
        future.setErrorResult(new ExceptionType.NotFoundError(String.format("UDF \"%s\" not found", udfName)));
        return future;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> insertUDF(String udf_name, InsertUDFRequest request) {
        int index = 0;
        for(index = 0; index < udfList.size(); index++){
            if(udf_name.equals(udfList.get(index).getName())) break;
        }
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        if(index == udfList.size()){
            UDFInfo newUDF = new UDFInfo(udf_name, request.getType(),true, System.currentTimeMillis(),System.currentTimeMillis(), request.getContent());
            udfList.add(newUDF);
            returnValue.setResult(new ResponseEntity<>(null, HttpStatus.CREATED));
            return returnValue;
        }
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> updateUDF(String udf_name, EditUDFRequest request) {
        int index = 0;
        for(index = 0; index < udfList.size(); index++){
            if(udf_name.equals(udfList.get(index).getName())) break;
        }
        int index_newName = 0;
        for(index_newName = 0; index_newName < udfList.size(); index_newName++){
            if(request.getName().equals(udfList.get(index_newName).getName())) break;
        }
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();

        if (index < udfList.size() && index_newName == udfList.size()){
            UDFInfo updateUDF = new UDFInfo(request.getName(), request.getType(),true, udfList.get(index).getCreatedOn(),System.currentTimeMillis(), request.getContent());
            udfList.set(index, updateUDF);
            returnValue.setResult(new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
            return returnValue;
        }


        returnValue.setErrorResult(new ExceptionType.BadRequestError(String.format("UDF name \"%s\" existed", request.getName())));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> delete(String udf_name) {
        int index = 0;
        for(index = 0; index < udfList.size(); index++){
            System.out.println("udf_name: "+ udfList.get(index).getName());
            if(udf_name.equals(udfList.get(index).getName())) break;
        }
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        if (index < udfList.size()){
            //UDFInfo updateUDF = new UDFInfo();
            udfList.remove(index);

            returnValue.setResult(new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
            return returnValue;
        }
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
        return returnValue;
    }
}
