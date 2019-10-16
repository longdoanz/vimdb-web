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
    @ApiModelProperty(value= "mertrics", example = "  ")
    private String mertrics;
    @ApiModelProperty(value= "servers", example = " ")
    private String servers;

    public String getMertrics() {
        return mertrics;
    }

    public String getServers() {
        return servers;
    }

    public void setMertrics(String mertrics) {
        this.mertrics = mertrics;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }


}
