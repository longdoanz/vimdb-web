package com.viettel.imdb.rest.service;

import com.viettel.imdb.rest.model.StatisticFilter;
import org.pmw.tinylog.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service
public class StatisticServiceImpl implements StatisticService{
    public DeferredResult<ResponseEntity<?>> getMetrics(StatisticFilter statisticFilter){
        Logger.error("Get metric");
        return null;
    }
    public DeferredResult<ResponseEntity<?>> getStatistics(StatisticFilter statisticFilter){
        Logger.error("get Statistics");
        return null;
    }
}
