package com.viettel.imdb.rest.model;


import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
//@ApiModel(value="StatisticResponse", description = "Statistic Response")
public class StatisticResponse {
    @ApiModelProperty(value= "node", example = "node")
    private String node;
    @ApiModelProperty(value= "metrics", example = "metrics list")
    private List<MetricValue> metrics;

    public StatisticResponse() {
    }

    public StatisticResponse(String node, List<MetricValue> metrics) {
        this.node = node;
        this.metrics = metrics;
    }

    public StatisticResponse(String node) {
        this.node = node;
        metrics = new ArrayList<>();
    }

    public void addNewMetric(String name, String type) {
        metrics.add(new MetricValue(name, type));
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public List<MetricValue> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<MetricValue> metrics) {
        this.metrics = metrics;
    }

    @Override
    public String toString() {
        return "StatisticResponse{" +
                "node='" + node + '\'' +
                ", metrics=" + metrics +
                '}';
    }

    public MetricValue getMetricByName(String metricName) {
        for(MetricValue re : metrics)
            if(re.getName().equals(metricName))
                return re;

        return null;
    }

    //    @NoArgsConstructor
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @ToString
//    @ApiModel(value="MetricValue", description = "Metric Value")
    public static class MetricValue{
        @ApiModelProperty(value= "name", example = "metrics list")
        private String name;
        @ApiModelProperty(value= "type", example = "metrics type")
        private String type;
        @ApiModelProperty(value= "value", example = "metrics value")
        private long value;

        public MetricValue() {
        }

    public MetricValue(String name, String type) {
        this.name = name;
        this.type = type;
        this.value = 0;
        // todo set value to default (0) :-?
    }

    public MetricValue(String name, String type, long value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MetricValue{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
}
