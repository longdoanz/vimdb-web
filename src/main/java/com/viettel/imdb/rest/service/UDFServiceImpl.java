package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.model.*;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class UDFServiceImpl implements UDFService{

    List<UDFInfo> UDFList;
    UDFRespone udfRespone = new UDFRespone(UDFList);
    @Autowired
    public UDFServiceImpl(List<UDFInfo> udfInfoList) {
        this.UDFList = udfInfoList;
    }


    @Override
    public DeferredResult<ResponseEntity<?>> getUDFs() {
        Logger.info("getUDFs(     )");
//        for (int i = 0; i < 10; i++){
//            UDFInfo UDF = new UDFInfo();
//            UDFList.add(UDF);
//        }
        BackupRequest backupRequest = new BackupRequest();
        DeferredResult res = new DeferredResult<>();
        res.setResult(UDFList);
        return res;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> insertUDF(String udf_name, InsertUDFRequest request) {
        int index = 0;
        for(index = 0; index < UDFList.size(); index++){
            if(udf_name.equals(UDFList.get(index).getName())) break;
        }
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        if(index == UDFList.size()){
            UDFInfo newUDF = new UDFInfo(udf_name, request.getType(),true, System.currentTimeMillis(),System.currentTimeMillis(), request.getContent());
            UDFList.add(newUDF);
            returnValue.setResult(new ResponseEntity<>(null, HttpStatus.CREATED));
            return returnValue;
        }
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
        return returnValue;
        //return null;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> updateUDF(String udf_name, EditUDFRequest request) {
        int index = 0;
        for(index = 0; index < UDFList.size(); index++){
            if(udf_name.equals(UDFList.get(index).getName())) break;
        }
        int index_newName = 0;
        for(index_newName = 0; index_newName < UDFList.size(); index_newName++){
            if(request.getName().equals(UDFList.get(index_newName).getName())) break;
        }
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();

        if (index < UDFList.size() && index_newName == UDFList.size()){
            UDFInfo updateUDF = new UDFInfo(request.getName(), request.getType(),true, UDFList.get(index).getCreatedOn(),System.currentTimeMillis(), request.getContent());
            UDFList.set(index, updateUDF);
            returnValue.setResult(new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
            return returnValue;
        }

        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> delete(String udf_name) {
        int index = 0;
        for(index = 0; index < UDFList.size(); index++){
            System.out.println("udf_name: "+ UDFList.get(index).getName());
            if(udf_name.equals(UDFList.get(index).getName())) break;
        }
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        if (index < UDFList.size()){
            //UDFInfo updateUDF = new UDFInfo();
            UDFList.remove(index);

            returnValue.setResult(new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
            return returnValue;
        }
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
        return returnValue;
    }
}
