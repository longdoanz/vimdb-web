package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.mock.client.ClientSimulator;
import com.viettel.imdb.rest.model.*;
import org.pmw.tinylog.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class UDFServiceImpl implements UDFService{

    List<UDFInfo> UDFList = new ArrayList<UDFInfo>();
    UDFRespone udfRespone = new UDFRespone(UDFList);


    @Override
    public DeferredResult<ResponseEntity<?>> getUDFs() {
        Logger.error("getUDFs(     )");
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
    public DeferredResult<ResponseEntity<?>> insertUDF(InsertUDFRequest request) {
        int index = 0;
        for(index = 0; index < UDFList.size(); index++){
            if(request.getUdf_name().equals(UDFList.get(index).getFileName())) break;
        }
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        if(index == UDFList.size()){
            UDFInfo newUDF = new UDFInfo(request.getUdf_name(), request.getType(),request.isSyncedOnAllNodes(), System.currentTimeMillis(),System.currentTimeMillis(), request.getContent());
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
            if(udf_name.equals(UDFList.get(index).getFileName())) break;
        }
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        if (index < UDFList.size()){
            UDFInfo updateUDF = new UDFInfo(request.getUdf_name(), request.getType(),request.isSyncedOnAllNodes(), UDFList.get(index).getCreateon(),System.currentTimeMillis(), request.getContent());
            UDFList.set(index, updateUDF);
            returnValue.setResult(new ResponseEntity<>(null, HttpStatus.CREATED));
            return returnValue;
        }

        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> delete(String udfname) {
        int index = 0;
        for(index = 0; index < UDFList.size(); index++){
            if(udfname.equals(UDFList.get(index).getFileName())) break;
        }
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        if (index < UDFList.size()){
            UDFInfo updateUDF = new UDFInfo();
            UDFList.remove(index);

            returnValue.setResult(new ResponseEntity<>(null, HttpStatus.CREATED));
            return returnValue;
        }
        returnValue.setResult(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
        return returnValue;
    }
}
