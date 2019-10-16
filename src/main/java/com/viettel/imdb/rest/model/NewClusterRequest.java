package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="NewClusterRequest", description = "Request to create cluster")
public class NewClusterRequest {
    @ApiModelProperty(value= "cluster Name", example = "cluster_name")
    private String clusterName;
    @ApiModelProperty(value= "restServerIp", example = "12.325.6.3")
    private String restServerIp;
    @ApiModelProperty(value= "ClusterAuthenInfo", example = "Cluster Authen Info")
    private AddClusterNodeRequest.ClusterAuthenInfo authenInfo;
}
