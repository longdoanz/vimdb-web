package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author quannh22
 * @since 09/09/2019
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="RemoveClusterNodeRequest", description = "Request to remove a node to the current managed cluster")
public class RemoveClusterNodeRequest {
    @ApiModelProperty(value= "SSHInfo of the server. Should change to SSH key in future", example = "ClusterNodeSSHInfo")
    private AddClusterNodeRequest.ClusterNodeSSHInfo sshInfo;
    @ApiModelProperty(value = "vIMDB server delete node", example = "TOO LONG TO DESCRIBE HERE")
    private VIMDBDeleteNodeInfo vimdbDeleteNodeInfo;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "VIMDBDeleteNodeInfo", description = "vIMDB server configuration")
    public class VIMDBDeleteNodeInfo {
        @ApiModelProperty(value = " Host ip", example = "172.16.28.123")
        private String host;

        private int port;
        @ApiModelProperty(value = "Cluster authentication info", example = "AuthenInfo")
        private AddClusterNodeRequest.ClusterAuthenInfo authenInfo;
    }

}
