package com.viettel.imdb.rest.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="StatisticResponse", description = "Statistic Response")
public class StatisticResponse {
    @ApiModelProperty(value= "node", example = "node")
    private String node;
    @ApiModelProperty(value= "metrics", example = "metrics list")
    private List<MetricValue> metrics;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value="MetricValue", description = "Metric Value")
    class MetricValue{
        @ApiModelProperty(value= "name", example = "metrics list")
        private String name;
        @ApiModelProperty(value= "type", example = "metrics type")
        private String type;
        @ApiModelProperty(value= "value", example = "metrics value")
        private String value;
    }
}
