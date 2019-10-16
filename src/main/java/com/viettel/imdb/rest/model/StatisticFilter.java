package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="StatisticFilter", description = "StatisticFilter")
public class StatisticFilter {
    @ApiModelProperty(value= "metrics", example = "  ")
    private String metrics;
    @ApiModelProperty(value= "servers", example = " ")
    private String servers;

    public String getMetrics() {
        return metrics;
    }

    public String getServers() {
        return servers;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }


}
