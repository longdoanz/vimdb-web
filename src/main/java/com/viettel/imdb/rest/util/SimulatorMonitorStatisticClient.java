package com.viettel.imdb.rest.util;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.rest.model.MetricResponse;
import com.viettel.imdb.rest.model.StatisticResponse;
import io.trane.future.Future;
import org.pmw.tinylog.Logger;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author quannh22
 * @since 16/10/2019
 */
public class SimulatorMonitorStatisticClient implements StatisticClient {
    private static final Random RANDOM = new SecureRandom();
    public static final String METRIC_TYPE_GAUGE = "gauge";
    public static final List<String> PREDEFINED_METRIC_NAMES = Arrays.asList(
            "system_total_ram", // todo should fix
            "system_total_disk", // todo should fix
            "vimdb_memory_used_ram",
            "vimdb_memory_used_disk",

            "vimdb_request_total",
            "vimdb_request_total_success",
            "vimdb_request_total_failed",
            
            "vimdb_request_read_total",
            "vimdb_request_read_success",
            "vimdb_request_read_failed",
            
            "vimdb_request_write_total",
            "vimdb_request_write_success",
            "vimdb_request_write_failed",
            
            "vimdb_request_delete_total",
            "vimdb_request_delete_success",
            "vimdb_request_delete_failed",

            "vimdb_request_scan_total",
            "vimdb_request_scan_success",
            "vimdb_request_scan_failed",

            "vimdb_request_avg_latency_in_us", // todo this is float (ms) - convert to us to be float
            "vimdb_request_latency_max_in_us", // todo this is float (ms)
            "vimdb_request_latency_read_under_1ms_count",
            "vimdb_request_latency_read_over_1ms_count",
            "vimdb_request_latency_read_over_2ms_count",
            "vimdb_request_latency_read_over_4ms_count",
            "vimdb_request_latency_read_over_8ms_count",
            "vimdb_request_latency_read_over_16ms_count",
            "vimdb_request_latency_read_over_32ms_count",
            "vimdb_request_latency_read_max",

            "vimdb_request_latency_write_under_1ms_count",
            "vimdb_request_latency_write_over_1ms_count",
            "vimdb_request_latency_write_over_2ms_count",
            "vimdb_request_latency_write_over_4ms_count",
            "vimdb_request_latency_write_over_8ms_count",
            "vimdb_request_latency_write_over_16ms_count",
            "vimdb_request_latency_write_over_32ms_count",
            "vimdb_request_latency_write_max",

            "vimdb_request_latency_delete_under_1ms_count",
            "vimdb_request_latency_delete_over_1ms_count",
            "vimdb_request_latency_delete_over_2ms_count",
            "vimdb_request_latency_delete_over_4ms_count",
            "vimdb_request_latency_delete_over_8ms_count",
            "vimdb_request_latency_delete_over_16ms_count",
            "vimdb_request_latency_delete_over_32ms_count",
            "vimdb_request_latency_delete_max",

            "vimdb_request_latency_scan_under_1ms_count",
            "vimdb_request_latency_scan_over_1ms_count",
            "vimdb_request_latency_scan_over_2ms_count",
            "vimdb_request_latency_scan_over_4ms_count",
            "vimdb_request_latency_scan_over_8ms_count",
            "vimdb_request_latency_scan_over_16ms_count",
            "vimdb_request_latency_scan_over_32ms_count",
            "vimdb_request_latency_scan_max",

            "vimdb_data_table_count",
            "vimdb_data_key_count"



    );
    public static Map<String, MetricResponse> metricNameMap; // todo this is a FIXED list
    private Map<String, NodeStatistic> nodeToMetricMap;
    static {
        metricNameMap = new ConcurrentHashMap<>();
        // todo add some predefined metric names - all of type GAUGE
        for(String metricName : PREDEFINED_METRIC_NAMES)
            metricNameMap.putIfAbsent(metricName, new MetricResponse(metricName, METRIC_TYPE_GAUGE));
    }
    public SimulatorMonitorStatisticClient() {
        nodeToMetricMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addNode(String host) {
        nodeToMetricMap.putIfAbsent(host, new NodeStatistic(host));
    }

    @Override
    public void removeNode(String host) {
        nodeToMetricMap.remove(host);
    }

    // todo update statistics to RANDOM nodes here :-???
    // todo USE THIS
    public void updateStatisticValueToRandomNode(String statisticName, long value) {
        try {
            List<String> hostList = new ArrayList<>(nodeToMetricMap.keySet());
            int index = RANDOM.nextInt(hostList.size());
            updateStatisticValueToSpecificNode(hostList.get(index), statisticName, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // todo update statistics to SPECIFIC nodes here :-???
    // todo USE THIS
    public void updateStatisticValueToSpecificNode(String host, String statisticName, long value) {
        if(!nodeToMetricMap.containsKey(host)) {
            Logger.error("Try to add statistic of a NOT_EXIST node");
        }
        NodeStatistic nodeStatistic = nodeToMetricMap.get(host);
        nodeStatistic.updateMetrics(statisticName, value);
    }

    // todo update statistics to RANDOM nodes here :-???
    // todo USE THIS
    public void addStatisticValueToRandomNode(String statisticName, long value) {
        try {
            List<String> hostList = new ArrayList<>(nodeToMetricMap.keySet());
            int index = RANDOM.nextInt(hostList.size());
            addStatisticValueToSpecificNode(hostList.get(index), statisticName, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // todo update statistics to SPECIFIC nodes here :-???
    // todo USE THIS
    public void addStatisticValueToSpecificNode(String host, String statisticName, long value) {
        if(!nodeToMetricMap.containsKey(host)) {
            Logger.error("Try to add statistic of a NOT_EXIST node");
        }
        NodeStatistic nodeStatistic = nodeToMetricMap.get(host);
        nodeStatistic.addMetrics(statisticName, value);
    }

    // todo USE THIS
    public void addLatencyInUsToRandomNode(String category, long value) {
        try {
            List<String> hostList = new ArrayList<>(nodeToMetricMap.keySet());
            int index = RANDOM.nextInt(hostList.size());
            addLatencyInUsToSpecificNode(hostList.get(index), category, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addLatencyInUsToSpecificNode(String host, String category, long value) {
        if(!nodeToMetricMap.containsKey(host)) {
            Logger.error("Try to add statistic of a NOT_EXIST node");
        }
        NodeStatistic nodeStatistic = nodeToMetricMap.get(host);
        nodeStatistic.addALatencyInUs(category, value);
    }

    @Override
    public Future<List<MetricResponse>> getMetrics(List<String> serverList) {
        List<MetricResponse> metricResponseList = new ArrayList<>(metricNameMap.values());
        return Future.value(metricResponseList);
    }

    @Override
    public Future<List<StatisticResponse>> getStatistics(List<String> serverList, List<String> metricList) {
        List<StatisticResponse> re = new ArrayList<>();
        if(serverList == null || serverList.isEmpty()) {
            serverList = new ArrayList<>(nodeToMetricMap.keySet());
        }
        if(metricList == null || metricList.isEmpty()) {
            metricList = new ArrayList<>(metricNameMap.keySet());
        }
        for(String server : serverList) {
            StatisticResponse reOfThisServer = new StatisticResponse();
            reOfThisServer.setNode(server);

            NodeStatistic nodeStatistic = nodeToMetricMap.get(server);
            if(nodeStatistic == null) {
                Logger.error("Node statistic is null with nodeToMetricMap {} and server {} --- serverList {}", nodeToMetricMap, server, serverList);
                return Future.exception(new ClientException(ErrorCode.KEY_LENGTH_INVALID));
            }
            List<StatisticResponse.MetricValue> allMetrics = nodeStatistic.getNodeStatistic().getMetrics();
            List<String> finalMetricList = metricList;
            List<StatisticResponse.MetricValue> reMetrics = allMetrics.stream().filter(metricValue -> finalMetricList.contains(metricValue.getName())).collect(Collectors.toList());
            reOfThisServer.setMetrics(reMetrics);

            re.add(reOfThisServer);
        }

        return Future.value(re);
    }

    public static class NodeStatistic {
        private String host;
        private StatisticResponse nodeStatistic;
        // latency area
        private AtomicLong totalLatencyInUs;
        private AtomicLong countLatency;

        public NodeStatistic(String host) {
            this.host = host;
            nodeStatistic = new StatisticResponse(host);
            for(MetricResponse metricResponse : SimulatorMonitorStatisticClient.metricNameMap.values()) {
                nodeStatistic.addNewMetric(metricResponse.getName(), metricResponse.getType());
            }
            addMetrics("system_total_ram", 100000000); // 100GB RAM
            addMetrics("system_total_disk", 1000000000); // 1000GB DISK
            totalLatencyInUs = new AtomicLong(0);
            countLatency = new AtomicLong(0);
        }

        public StatisticResponse.MetricValue getMetricValue(String metricName) {
            return nodeStatistic.getMetricByName(metricName);
        }

        public NodeStatistic(String host, StatisticResponse nodeStatistic) {
            this.host = host;
            this.nodeStatistic = nodeStatistic;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public StatisticResponse getNodeStatistic() {
            return nodeStatistic;
        }

        public void setNodeStatistic(StatisticResponse nodeStatistic) {
            this.nodeStatistic = nodeStatistic;
        }

        @Override
        public String toString() {
            return "NodeStatistic{" +
                    "host='" + host + '\'' +
                    ", nodeStatistic=" + nodeStatistic +
                    '}';
        }

        public void updateMetrics(String statisticName, long value) {
            StatisticResponse.MetricValue metricValue = getMetricValue(statisticName);
            if(metricValue == null) {
                Logger.error("Metric {} is NULL - do nothing", statisticName);
                return;
            }
            switch (statisticName) {
                case "system_total_ram":
                    metricValue.setValue(value);
                    break;
                case "system_total_disk":
                    metricValue.setValue(value);
                    break;
                case "vimdb_memory_used_ram":
                    metricValue.setValue(value);
                    break;
                case "vimdb_memory_used_disk":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_total":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_total_success":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_total_failed":
                    metricValue.setValue(value);
                    break;

                case "vimdb_request_read_total":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_read_success":
                    metricValue.setValue(value);
                    increaseSINGLEMetricValue("vimdb_request_read_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_success",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;
                case "vimdb_request_read_failed":
                    metricValue.setValue(value);
                    increaseSINGLEMetricValue("vimdb_request_read_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_failed",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;

                case "vimdb_request_write_total":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_write_success":
                    metricValue.setValue(value);
                    increaseSINGLEMetricValue("vimdb_request_write_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_success",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;
                case "vimdb_request_write_failed":
                    metricValue.setValue(value);
                    increaseSINGLEMetricValue("vimdb_request_write_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_failed",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;


                case "vimdb_request_delete_total":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_delete_success":
                    metricValue.setValue(value);
                    increaseSINGLEMetricValue("vimdb_request_delete_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_success",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;
                case "vimdb_request_delete_failed":
                    metricValue.setValue(value);
                    increaseSINGLEMetricValue("vimdb_request_delete_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_failed",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;

                case "vimdb_request_scan_total":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_scan_success":
                    metricValue.setValue(value);
                    increaseSINGLEMetricValue("vimdb_request_scan_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_success",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;
                case "vimdb_request_scan_failed":
                    metricValue.setValue(value);
                    increaseSINGLEMetricValue("vimdb_request_scan_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_failed",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;

                    // todo latency should not be updated here, update it in addALatencyInUs(us)
                case "vimdb_request_avg_latency_in_us":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_max_in_us":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_read_under_1ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_read_over_1ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_read_over_2ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_read_over_4ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_read_over_8ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_read_over_16ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_read_over_32ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_read_max":
                    metricValue.setValue(value);
                    break;

                case "vimdb_request_latency_write_under_1ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_write_over_1ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_write_over_2ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_write_over_4ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_write_over_8ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_write_over_16ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_write_over_32ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_write_max":
                    metricValue.setValue(value);
                    break;

                case "vimdb_request_latency_delete_under_1ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_delete_over_1ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_delete_over_2ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_delete_over_4ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_delete_over_8ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_delete_over_16ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_delete_over_32ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_delete_max":
                    metricValue.setValue(value);
                    break;

                case "vimdb_request_latency_scan_under_1ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_scan_over_1ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_scan_over_2ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_scan_over_4ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_scan_over_8ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_scan_over_16ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_scan_over_32ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_scan_max":
                    metricValue.setValue(value);
                    break;

                case "vimdb_data_table_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_data_key_count":
                    metricValue.setValue(value);
                    break;
                default:
                    break;
            }
        }

        public void addMetrics(String statisticName, long value) {
            StatisticResponse.MetricValue metricValue = getMetricValue(statisticName);
            if(metricValue == null) {
                Logger.error("Metric {} is NULL - do nothing", statisticName);
                return;
            }
            switch (statisticName) {
                case "system_total_ram":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "system_total_disk":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_memory_used_ram":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_memory_used_disk":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_total":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_total_success":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_total_failed":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;

                case "vimdb_request_read_total":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_read_success":
                    metricValue.setValue(metricValue.getValue() + value);
                    increaseSINGLEMetricValue("vimdb_request_read_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_success",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;
                case "vimdb_request_read_failed":
                    metricValue.setValue(metricValue.getValue() + value);
                    increaseSINGLEMetricValue("vimdb_request_read_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_failed",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;

                case "vimdb_request_write_total":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_write_success":
                    metricValue.setValue(metricValue.getValue() + value);
                    increaseSINGLEMetricValue("vimdb_request_write_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_success",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;
                case "vimdb_request_write_failed":
                    metricValue.setValue(metricValue.getValue() + value);
                    increaseSINGLEMetricValue("vimdb_request_write_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_failed",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;


                case "vimdb_request_delete_total":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_delete_success":
                    metricValue.setValue(metricValue.getValue() + value);
                    increaseSINGLEMetricValue("vimdb_request_delete_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_success",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;
                case "vimdb_request_delete_failed":
                    metricValue.setValue(metricValue.getValue() + value);
                    increaseSINGLEMetricValue("vimdb_request_delete_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_failed",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;

                case "vimdb_request_scan_total":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_scan_success":
                    metricValue.setValue(metricValue.getValue() + value);
                    increaseSINGLEMetricValue("vimdb_request_scan_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_success",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;
                case "vimdb_request_scan_failed":
                    metricValue.setValue(metricValue.getValue() + value);
                    increaseSINGLEMetricValue("vimdb_request_scan_total",1);
                    increaseSINGLEMetricValue("vimdb_request_total_failed",1);
                    increaseSINGLEMetricValue("vimdb_request_total",1);
                    break;

                // todo latency should not be updated here, update it in addALatencyInUs(us)
                case "vimdb_request_avg_latency_in_us":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_max_in_us":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_read_under_1ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_read_over_1ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_read_over_2ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_read_over_4ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_read_over_8ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_read_over_16ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_read_over_32ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_read_max":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;

                case "vimdb_request_latency_write_under_1ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_write_over_1ms_count":
                    metricValue.setValue(value);
                    break;
                case "vimdb_request_latency_write_over_2ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_write_over_4ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_write_over_8ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_write_over_16ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_write_over_32ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_write_max":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;

                case "vimdb_request_latency_delete_under_1ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_delete_over_1ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_delete_over_2ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_delete_over_4ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_delete_over_8ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_delete_over_16ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_delete_over_32ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_delete_max":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;

                case "vimdb_request_latency_scan_under_1ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_scan_over_1ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_scan_over_2ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_scan_over_4ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_scan_over_8ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_scan_over_16ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_scan_over_32ms_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_request_latency_scan_max":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;

                case "vimdb_data_table_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                case "vimdb_data_key_count":
                    metricValue.setValue(metricValue.getValue() + value);
                    break;
                default:
                    break;
            }
        }


        public void setSINGLEMetricValue(String statisticName, long value) {
            StatisticResponse.MetricValue metricValue = getMetricValue(statisticName);
            if(metricValue == null) {
                Logger.error("Metric {} is NULL - do nothing", statisticName);
                return;
            }
            metricValue.setValue(value);
        }

        public void increaseSINGLEMetricValue(String statisticName, long delta) {
            StatisticResponse.MetricValue metricValue = getMetricValue(statisticName);
            if(metricValue == null) {
                Logger.error("Metric {} is NULL - do nothing", statisticName);
                return;
            }
            metricValue.setValue(metricValue.getValue() + delta);
        }

        public void addALatencyInUs(String category, long latencyInUs) {
            // category: "read", "write", "delete", "scan"
            countLatency.getAndIncrement();
            totalLatencyInUs.getAndAdd(latencyInUs);
            // max latency
            StatisticResponse.MetricValue maxLatencyMetricValue = getMetricValue("vimdb_request_latency_max_in_us");
            if(latencyInUs > maxLatencyMetricValue.getValue())
                maxLatencyMetricValue.setValue(latencyInUs);
            // avg latency
            StatisticResponse.MetricValue avgLatencyMetricValue = getMetricValue("vimdb_request_avg_latency_in_us");
            avgLatencyMetricValue.setValue(totalLatencyInUs.get() / countLatency.get());
            // latency by category
            switch (category) {
                case "read":
                    if(latencyInUs < 1000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_read_under_1ms_count", 1);
                    } else if(latencyInUs < 2000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_read_over_1ms_count", 1);
                    } else if(latencyInUs < 4000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_read_over_2ms_count", 1);
                    } else if(latencyInUs < 8000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_read_over_4ms_count", 1);
                    } else if(latencyInUs < 16000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_read_over_8ms_count", 1);
                    } else if(latencyInUs < 32000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_read_over_16ms_count", 1);
                    } else {
                        increaseSINGLEMetricValue("vimdb_request_latency_read_over_32ms_count", 1);
                    }
                    StatisticResponse.MetricValue readMaxLatency = getMetricValue("vimdb_request_latency_read_max");
                    if(readMaxLatency.getValue() < latencyInUs) {
                        readMaxLatency.setValue(latencyInUs);
                    }
                    break;
                case "write":
                    if(latencyInUs < 1000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_write_under_1ms_count", 1);
                    } else if(latencyInUs < 2000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_write_over_1ms_count", 1);
                    } else if(latencyInUs < 4000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_write_over_2ms_count", 1);
                    } else if(latencyInUs < 8000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_write_over_4ms_count", 1);
                    } else if(latencyInUs < 16000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_write_over_8ms_count", 1);
                    } else if(latencyInUs < 32000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_write_over_16ms_count", 1);
                    } else {
                        increaseSINGLEMetricValue("vimdb_request_latency_write_over_32ms_count", 1);
                    }
                    StatisticResponse.MetricValue writeMaxLatency = getMetricValue("vimdb_request_latency_write_max");
                    if(writeMaxLatency.getValue() < latencyInUs) {
                        writeMaxLatency.setValue(latencyInUs);
                    }
                    break;
                case "delete":
                    if(latencyInUs < 1000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_delete_under_1ms_count", 1);
                    } else if(latencyInUs < 2000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_delete_over_1ms_count", 1);
                    } else if(latencyInUs < 4000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_delete_over_2ms_count", 1);
                    } else if(latencyInUs < 8000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_delete_over_4ms_count", 1);
                    } else if(latencyInUs < 16000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_delete_over_8ms_count", 1);
                    } else if(latencyInUs < 32000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_delete_over_16ms_count", 1);
                    } else {
                        increaseSINGLEMetricValue("vimdb_request_latency_delete_over_32ms_count", 1);
                    }
                    StatisticResponse.MetricValue deleteMaxLatency = getMetricValue("vimdb_request_latency_delete_max");
                    if(deleteMaxLatency.getValue() < latencyInUs) {
                        deleteMaxLatency.setValue(latencyInUs);
                    }
                    break;
                case "scan":
                    if(latencyInUs < 1000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_scan_under_1ms_count", 1);
                    } else if(latencyInUs < 2000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_scan_over_1ms_count", 1);
                    } else if(latencyInUs < 4000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_scan_over_2ms_count", 1);
                    } else if(latencyInUs < 8000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_scan_over_4ms_count", 1);
                    } else if(latencyInUs < 16000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_scan_over_8ms_count", 1);
                    } else if(latencyInUs < 32000) {
                        increaseSINGLEMetricValue("vimdb_request_latency_scan_over_16ms_count", 1);
                    } else {
                        increaseSINGLEMetricValue("vimdb_request_latency_scan_over_32ms_count", 1);
                    }
                    StatisticResponse.MetricValue scanMaxLatency = getMetricValue("vimdb_request_latency_scan_max");
                    if(scanMaxLatency.getValue() < latencyInUs) {
                        scanMaxLatency.setValue(latencyInUs);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

