package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.model.BackupRequest;
import com.viettel.imdb.rest.model.RestoreRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface BackupRestoreService {
    DeferredResult<ResponseEntity<?>> backup(BackupRequest request);
    DeferredResult<ResponseEntity<?>> backupProcessStatus(String process);

    DeferredResult<ResponseEntity<?>> restore(RestoreRequest request);
    DeferredResult<ResponseEntity<?>> restoreProcessStatus(String process);
}
