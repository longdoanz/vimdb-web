package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.model.StatisticFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface StatisticService {
    DeferredResult<ResponseEntity<?>> getMetrics(StatisticFilter statisticFilter);
    DeferredResult<ResponseEntity<?>> getStatistics(StatisticFilter statisticFilter);
}
