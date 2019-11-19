package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.exception.ExceptionType;
import com.viettel.imdb.rest.model.BackupRequest;
import com.viettel.imdb.rest.model.CallbackResponse;
import com.viettel.imdb.rest.model.ProcessStatus;
import com.viettel.imdb.rest.model.RestoreRequest;
import com.viettel.imdb.rest.sshconect.AuthenticationOption;
import org.pmw.tinylog.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;

@Service
public class BackupRestoreServiceImpl implements BackupRestoreService {
    private final String COMPLETED = "completed";
    private final String FAIL = "fail";
    private final String PROCESSING = "processing";
    public static final String BACKUP_RESTORE_LOCATION = "/home/imdb/vimdb2/scripts/test";

    class StateProcess {
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

    public void validateBackupConfig(BackupRequest.BackupConfig config) {
        if (config.isPartitionRange()) {
            if (config.getPartitionRangeStart() < 0 || config.getPartitionRangeStart() > 4095)
                throw new ExceptionType.BadRequestError("Partition Range Start Invalid");
            if (config.getPartitionRangeEnd() < 0 || config.getPartitionRangeEnd() > 4095)
                throw new ExceptionType.BadRequestError("Partition Range End Invalid");
            if (config.getPartitionRangeEnd() < config.getPartitionRangeStart())
                throw new ExceptionType.BadRequestError("Partition Range Invalid");
        } else {
            if (config.getParittionList() != null && config.getParittionList().size() != 0) {
                int max = Collections.max(config.getParittionList());
                int min = Collections.min(config.getParittionList());
                if (max > 4095 || max < 0 || min > 4095 || min < 0)
                    throw new ExceptionType.BadRequestError("Partition Range Invalid");
                Set<Integer> set = new HashSet<Integer>(config.getParittionList());
                if (set.size() < config.getParittionList().size())
                    throw new ExceptionType.BadRequestError("Partition Duplicate");
            }
        }
        if (config.getBackupDirectory() == null || config.getBackupDirectory().trim() == "")
            throw new ExceptionType.BadRequestError("Restore Directory Invalid");
        config.setBackupDirectory(config.getBackupDirectory().trim());
        String firstLetter = config.getBackupDirectory().substring(0, 1);
        if (!firstLetter.equals("/"))
            config.setBackupDirectory(BACKUP_RESTORE_LOCATION + "/" +
                    config.getBackupDirectory());
    }


    public void validateRestoreConfig(RestoreRequest.RestoreConfig config){
        if(config.isPartitionRange()){
            if(config.getPartitionRangeStart() < 0 || config.getPartitionRangeStart() > 4095)
                throw  new ExceptionType.BadRequestError("Partition Range Start Invalid");
            if(config.getPartitionRangeEnd() < 0 || config.getPartitionRangeEnd() > 4095)
                throw  new ExceptionType.BadRequestError("Partition Range End Invalid");
            if(config.getPartitionRangeEnd() < config.getPartitionRangeStart())
                throw  new ExceptionType.BadRequestError("Partition Range Invalid");
        }else{
            if(config.getParittionList() != null && config.getParittionList().size() != 0){
                int max = Collections.max(config.getParittionList());
                int min = Collections.min(config.getParittionList());
                if (max > 4095 || max < 0 || min > 4095 || min < 0 )
                    throw  new ExceptionType.BadRequestError("Partition Range Invalid");
                Set<Integer> set = new HashSet<Integer>(config.getParittionList());
                if(set.size() < config.getParittionList().size())
                    throw  new ExceptionType.BadRequestError("Partition Duplicate");
            }
        }

        if (config.getRestoreDirectory() == null || config.getRestoreDirectory().trim() == "")
            throw new ExceptionType.BadRequestError("Restore Directory Invalid");

        config.setRestoreDirectory(config.getRestoreDirectory().trim());
        String firstLetter = config.getRestoreDirectory().substring(0,1);
        if(!firstLetter.equals("/"))
            config.setRestoreDirectory(BACKUP_RESTORE_LOCATION+"/"+
                    config.getRestoreDirectory());

    }

    public void validateBackupRestoreClient(BackupRequest.ClusterNodeSSHInfo nodeSSHInfo) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        if (nodeSSHInfo.getIp() == null || !nodeSSHInfo.getIp().matches(PATTERN))
            throw new ExceptionType.BadRequestError("Ip address invlid");
        if (nodeSSHInfo.getPort() < 0 || nodeSSHInfo.getPort() > 65535)
            throw new ExceptionType.BadRequestError("Port invlid");
        if (nodeSSHInfo.getUsername() == null || nodeSSHInfo.getUsername().equals(""))
            throw new ExceptionType.BadRequestError("username invalid");
        if (nodeSSHInfo.getAuthenticationOption() == AuthenticationOption.password)
            if (nodeSSHInfo.getPassword() == null || nodeSSHInfo.getPassword().equals(""))
                throw new ExceptionType.BadRequestError("password is required");
        if (nodeSSHInfo.getAuthenticationOption() == AuthenticationOption.sshKey)
            if (nodeSSHInfo.getSsKey() == null || nodeSSHInfo.getSsKey().equals(""))
                throw new ExceptionType.BadRequestError("SSHKey is required");
    }

    Map<Long, StateProcess> processState = new HashMap<Long, StateProcess>();

    @Override
    public DeferredResult<ResponseEntity<?>> backup(BackupRequest request) {
        validateBackupConfig(request.getBackupConfig());
        validateBackupRestoreClient(request.getBackupClient());

        Random random = new Random();
        long backupProcessid = random.nextLong();
        if (backupProcessid < 0) backupProcessid = -backupProcessid;

        StateProcess stateProcess = new StateProcess("processing", System.currentTimeMillis(), random.nextInt(60000) + 20000, random.nextInt(60000) + 10000);
        processState.put(backupProcessid, stateProcess);
        //String callback = "/v1/tool/backup?process="+backupProcessid;
        String callback = String.valueOf(backupProcessid);
        System.out.println("callback: " + callback);
        System.out.println("State: timeProcess" + stateProcess.timeProcess + " timeout" + stateProcess.timeout);
        CallbackResponse callbackResponse = new CallbackResponse("backup?process=" + callback);
        DeferredResult returnValue = new DeferredResult<>();
        returnValue.setResult(callbackResponse);
        return returnValue;
    }

    @Override
    public ProcessStatus backupProcessStatus(String process) {
        //Timer timer = new Timer();

        long processId = Long.parseLong(process);
        Logger.info(process);
        StateProcess state = processState.get(processId);
        if(state == null) {
            return new ProcessStatus("completed");
        }
        String status = state.stateStatus;
        if (state.stateStatus.equals("failed")) {
            //returnValue.setResult("failed");
            status = FAIL;
        } else if (state.stateStatus.equals("completed")) {
            //returnValue.setResult("completed");
            status = COMPLETED;
        } else {
            //processing
            long exetime = System.currentTimeMillis() - state.timebegin;
            if (exetime < state.timeProcess) {
                //returnValue.setResult("processing");
                status = PROCESSING;
            } else if (exetime > state.timeProcess && state.timeProcess <= state.timeout) {
                status = COMPLETED;
            } else if (exetime > state.timeout && state.timeProcess > state.timeout) {
                status = FAIL;
            }

        }
        return new ProcessStatus(status);
    }

    @Override
    public DeferredResult<ResponseEntity<?>> restore(RestoreRequest request) {
        validateRestoreConfig(request.getRestoreConfig());
        validateBackupRestoreClient(request.getRestoreClient());

        Random random = new Random();
        long restoreProcessid = random.nextLong();
        if (restoreProcessid < 0) restoreProcessid = -restoreProcessid;
        StateProcess stateProcess = new StateProcess("processing", System.currentTimeMillis(), random.nextInt(60000) + 20000, random.nextInt(60000) + 10000);
        processState.put(restoreProcessid, stateProcess);
        //String callback = "/v1/tool/restore?process="+restoreProcessid;
        String callback = String.valueOf(restoreProcessid);
        System.out.println("callback: " + callback);
        System.out.println("State: timeProcess" + stateProcess.timeProcess + " timeout" + stateProcess.timeout);
        CallbackResponse callbackResponse = new CallbackResponse("restore?process=" + callback);
        DeferredResult returnValue = new DeferredResult<>();
        returnValue.setResult(callbackResponse);
        return returnValue;
    }

    @Override
    public ProcessStatus restoreProcessStatus(String process) {
        long processId = Long.parseLong(process);
        StateProcess state = processState.get(processId);
        if(state == null) {
            return new ProcessStatus("completed");
        }
        String status = state.stateStatus;

        if (state.stateStatus.equals("failed")) {
            //returnValue.setResult("failed");
            status = FAIL;
        } else if (state.stateStatus.equals("completed")) {
            //returnValue.setResult("completed");
            status = COMPLETED;
        } else {
            //processing
            long exetime = System.currentTimeMillis() - state.timebegin;
            if (exetime < state.timeProcess) {
                //returnValue.setResult("processing");
                status = PROCESSING;
            } else if (exetime > state.timeProcess && state.timeProcess <= state.timeout) {
                status = COMPLETED;
            } else if (exetime > state.timeout && state.timeProcess > state.timeout) {
                status = FAIL;
            }

        }
        return new ProcessStatus(status);
    }
}
