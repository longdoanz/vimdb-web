package com.viettel.imdb.rest.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface StatisticService {
    DeferredResult<ResponseEntity<?>> getMetrics(String servers);
    DeferredResult<ResponseEntity<?>> getStatistics(String servers, String metrics);
}
