package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author quannh22
 * @since 16/10/2019
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MetricResponse {
    @ApiModelProperty(value= "name", example = "metric name")
    private String name;
    @ApiModelProperty(value= "name", example = "metric")
    private String metric;
    @ApiModelProperty(value= "type", example = "metrics type")
    private String type;

    /*public MetricResponse() {
    }
*/
    public MetricResponse(String metric, String type) {
        this.metric = metric;
        this.type = type;

        this.name = metric.replace("_", " ");
    }
/*
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

    @Override
    public String toString() {
        return "MetricResponse{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }*/
}
