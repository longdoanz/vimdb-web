package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author quannh22
 * @since 16/10/2019
 */
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
public class MetricResponse {
    @ApiModelProperty(value= "name", example = "metrics list")
    private String name;
    @ApiModelProperty(value= "type", example = "metrics type")
    private String type;

    public MetricResponse() {
    }

    public MetricResponse(String name, String type) {
        this.name = name;
        this.type = type;
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

    @Override
    public String toString() {
        return "MetricResponse{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
