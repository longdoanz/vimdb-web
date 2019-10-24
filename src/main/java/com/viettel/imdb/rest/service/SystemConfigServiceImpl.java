package com.viettel.imdb.rest.service;

import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.model.EditUserRequest;
import io.trane.future.Future;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;

import static com.viettel.imdb.rest.common.Utils.restResultToDeferredResult;
import static com.viettel.imdb.rest.common.Utils.throwableToHttpStatus;

@Service
public class SystemConfigServiceImpl implements SystemConfigService{
    private final IMDBClient client;

    @Autowired
    public SystemConfigServiceImpl(IMDBClient client) {
        this.client = client;
    }
    @Override
    public DeferredResult<ResponseEntity<?>> currentUser(EditUserRequest request) {
        Logger.info("editUser({})", request);
        Future<Void> updateFuture = client.updateUser(request.getUserName(), request.getRoles()); // todo how about newRoles
        Future<Result> resultFuture = updateFuture
                .map(aVoid -> new Result(HttpStatus.NO_CONTENT))
                .rescue(throwable -> throwableToHttpStatus(throwable));
        return restResultToDeferredResult(resultFuture);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> getRestoreFile(String node, String backupDir) {
        List<String> listFile = new ArrayList<String>();
        listFile.add("backup1-2019123");
        listFile.add("backup1-2019124");
        listFile.add("backup1-2019125");
        listFile.add("backup1-2019126");
        listFile.add("backup1-2019127");
        DeferredResult res = new DeferredResult<>();
        res.setResult(listFile);
        return res;

    }
}
