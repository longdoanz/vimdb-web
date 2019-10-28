package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.model.BackupRequest;
import com.viettel.imdb.rest.model.CallbackResponse;
import com.viettel.imdb.rest.model.ProcessStatus;
import com.viettel.imdb.rest.model.RestoreRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class BackupRestoreServiceImpl implements BackupRestoreService {
    private final String COMPLETED = "completed";
    private final String FAIL = "failed";
    private final String PROCESSING = "processing";
    class StateProcess{
        String stateStatus;
        long timebegin;
        int timeout;
        int timeProcess;

        public StateProcess(String stateStatus, long timebegin, int timeout, int timeProcess) {
            this.stateStatus = stateStatus;
            this.timebegin = timebegin;
            this.timeout = timeout;
            this.timeProcess = timeProcess;
        }
    }
    Map<Long, StateProcess> processState = new HashMap<>();
    @Override
    public DeferredResult<ResponseEntity<?>> backup(BackupRequest request) {
        System.out.println("akejfbkajwebfk");
        Random random = new Random();
        long backupProcessid = random.nextLong();
        if (backupProcessid < 0) backupProcessid = -backupProcessid;

        StateProcess stateProcess = new StateProcess("processing", System.currentTimeMillis(), random.nextInt(60000)+20000, random.nextInt(50000)+20000);
        processState.put(backupProcessid, stateProcess);
        //String callback = "/v1/tool/backup?process="+backupProcessid;
        String callback = String.valueOf(backupProcessid);
        System.out.println("callback: "+ callback);
        System.out.println("State: timeProcess"+ stateProcess.timeProcess +" timeout" + stateProcess.timeout);
        CallbackResponse callbackResponse = new CallbackResponse("/v1/tool/backup?process="+callback);
        DeferredResult returnValue = new DeferredResult<>();
        returnValue.setResult(callbackResponse);
        return returnValue;
    }

    @Override
    public DeferredResult<?> backupProcessStatus(String process) {
        //Timer timer = new Timer();

        long processId = Long.parseLong(process);
        StateProcess state = processState.get(processId);
        DeferredResult returnValue = new DeferredResult<>();

        if(state == null) {
            returnValue.setResult(new ProcessStatus(COMPLETED));
            return returnValue;
        }

        String status = state.stateStatus;
        if(state.stateStatus.equals("failed")){
            //returnValue.setResult("failed");
            status = FAIL;
        }else if(state.stateStatus.equals("completed")){
            //returnValue.setResult("completed");
            status = COMPLETED;
        }else {
            //processing
            long exetime = System.currentTimeMillis() - state.timebegin;
            if(exetime < state.timeProcess){
                //returnValue.setResult("processing");
                status = PROCESSING;
            }else if(exetime > state.timeProcess && state.timeProcess <= state.timeout){
                status = COMPLETED;
            }else if(exetime > state.timeout && state.timeProcess > state.timeout){
                status = FAIL;
            }

        }
        ProcessStatus processStatus = new ProcessStatus(status);
        returnValue.setResult(processStatus);
        System.out.println(status);
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> restore(RestoreRequest request) {
        Random random = new Random();
        long restoreProcessid = random.nextLong();
        if(restoreProcessid < 0) restoreProcessid = -restoreProcessid;
        StateProcess stateProcess = new StateProcess("processing", System.currentTimeMillis(), random.nextInt(60000)+20000, random.nextInt(50000)+20000);
        processState.put(restoreProcessid, stateProcess);
        //String callback = "/v1/tool/restore?process="+restoreProcessid;
        String callback = String.valueOf(restoreProcessid);
        System.out.println("callback: "+ callback);
        System.out.println("State: timeProcess"+ stateProcess.timeProcess +" timeout" + stateProcess.timeout);
        CallbackResponse callbackResponse = new CallbackResponse("/v1/tool/restore?process="+callback);
        DeferredResult returnValue = new DeferredResult<>();
        returnValue.setResult(callbackResponse);
        return returnValue;
    }

    @Override
    public DeferredResult<ResponseEntity<?>> restoreProcessStatus(String process) {
        long processId = Long.parseLong(process);
        StateProcess state = processState.get(processId);
        DeferredResult returnValue = new DeferredResult<>();
        String status = state.stateStatus;

        if(state.stateStatus.equals("failed")){
            //returnValue.setResult("failed");
            status = FAIL;
        }else if(state.stateStatus.equals("completed")){
            //returnValue.setResult("completed");
            status = COMPLETED;
        }else {
            //processing
            long exetime = System.currentTimeMillis() - state.timebegin;
            if(exetime < state.timeProcess){
                //returnValue.setResult("processing");
                status = PROCESSING;
            }else if(exetime > state.timeProcess && state.timeProcess <= state.timeout){
                status = COMPLETED;
            }else if(exetime > state.timeout && state.timeProcess > state.timeout){
                status = FAIL;
            }

        }
        ProcessStatus processStatus = new ProcessStatus(status);
        returnValue.setResult(processStatus);
        System.out.println(status);
        return returnValue;
    }
}
