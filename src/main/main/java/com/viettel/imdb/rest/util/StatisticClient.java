package com.viettel.imdb.rest.util;

import com.viettel.imdb.rest.model.MetricResponse;
import com.viettel.imdb.rest.model.StatisticResponse;
import io.trane.future.Future;

import java.util.List;

/**
 * @author quannh22
 * @since 16/10/2019
 */
public interface StatisticClient {
    // todo this is somehow equal to IMDBClient - to get statistics from vIMDB modules

    Future<List<MetricResponse>> getMetrics();

    Future<List<StatisticResponse>> getStatistics(List<String> serverList, List<String> metricList);


    void addNode(String host); // add a new host, initialize its own Statistic
    void removeNode(String host); // remove a host

    void updateStatisticValueToRandomNode(String statisticName, long value);
    void updateStatisticValueToSpecificNode(String host, String statisticName, long value);
    void addStatisticValueToRandomNode(String statisticName, long value);
    void addStatisticValueToSpecificNode(String host, String statisticName, long value);
    void addLatencyInUsToRandomNode(String category, long value);
    void addLatencyInUsToSpecificNode(String host, String category, long value);
}
