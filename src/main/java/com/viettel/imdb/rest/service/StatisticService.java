package com.viettel.imdb.rest.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

public interface StatisticService {
    DeferredResult<ResponseEntity<?>> getMetrics();
    DeferredResult<ResponseEntity<?>> getStatistics(List<String> servers, List<String> metrics);
}
