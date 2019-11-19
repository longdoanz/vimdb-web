package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.exception.ExceptionType;
import com.viettel.imdb.rest.model.EditUDFRequest;
import com.viettel.imdb.rest.model.InsertUDFRequest;
import com.viettel.imdb.rest.model.UDFInfo;
import org.pmw.tinylog.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UDFServiceImpl implements UDFService{

    Map<String, UDFInfo> udfInfoMap;
//    List<UDFInfo> udfList;

    /*@Autowired
    public UDFServiceImpl(List<UDFInfo> udfInfoList) {
        this.udfList = udfInfoList;
    }*/

    public UDFServiceImpl() {
        udfInfoMap = new ConcurrentHashMap<>();
    }


    @Override
    public List<UDFInfo> getUDFs() {
        Logger.info("getUDFs(     )");
//        for (int i = 0; i < 10; i++){
//            UDFInfo UDF = new UDFInfo();
//            UDFList.add(UDF);
//        }
        return new ArrayList<>(udfInfoMap.values());
    }

    @Override
    public UDFInfo getUdfByName(String udfName) {
        UDFInfo udf = udfInfoMap.get(udfName);
        if(udf == null) {
            throw new ExceptionType.NotFoundError("UDF_NOT_FOUND", udfName);
        }
        Logger.error("In Service {}",udf);
        return udf;
    }

    @Override
    public void insertUDF(String udf_name, InsertUDFRequest request) {
        if(udfInfoMap.get(udf_name) != null) {
            throw new ExceptionType.BadRequestError("UDF_EXISTED", udf_name);
        }
        UDFInfo newUDF = new UDFInfo(udf_name, request.getType(),true, System.currentTimeMillis(),System.currentTimeMillis(), request.getContent());
        if(udfInfoMap.putIfAbsent(udf_name, newUDF) != null)
            throw new ExceptionType.BadRequestError("UDF_EXISTED", udf_name);
    }

    @Override
    public void updateUDF(String udfName, EditUDFRequest request) {
        Logger.info("Request: {}", request);
        // Isert for preserve
        if(udfInfoMap.get(udfName) == null)
            throw new ExceptionType.BadRequestError("UDF_NOT_FOUND", udfName);

        UDFInfo newUdf = new UDFInfo();

        String newName = request.getName();
        if(newName != null && newName.equals(udfName))
            newName = null;
        if(newName != null) {
            UDFInfo oldUdf = udfInfoMap.putIfAbsent(request.getName(), newUdf);
            if(oldUdf != null)
                throw new ExceptionType.BadRequestError("UDF_EXISTED", udfName);
        }

        String finalNewName = newName;
        UDFInfo check = udfInfoMap.computeIfPresent(udfName, (key, oldValue) -> {
            newUdf.setName(finalNewName == null ? oldValue.getName() : request.getName());

            newUdf.setType(request.getType() == null ? oldValue.getType() : request.getType());

            newUdf.setContent(request.getContent() == null ? oldValue.getContent() : request.getContent());

            if(finalNewName != null)
                udfInfoMap.remove(key);
            return newUdf;
        });

        if(check == null) {
            udfInfoMap.remove(request.getName());
            throw new ExceptionType.BadRequestError("UDF_NOT_FOUND", udfName);
        }
    }

    @Override
    public void delete(String udfName) {
        if(udfInfoMap.remove(udfName) == null)
            throw new ExceptionType.NotFoundError(String.format("UDF \"%s\" not found", udfName));
    }
}
