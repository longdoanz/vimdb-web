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
@ApiModel(value="RestoreRequest", description = "Request to remove a node to the current managed cluster")
public class RestoreRequest {
    @ApiModelProperty(value = " clusterAuthInfo", example = "")
    private ClusterAuthInfo clusterAuthInfo;
    @ApiModelProperty(value = " clusterNodeSSHInfo", example = "")
    private ClusterNodeSSHInfo clusterNodeSSHInfo;
    @ApiModelProperty(value = " restoreConfig", example = "")
    private RestoreConfig restoreConfig;


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "ClusterAuthInfo", description = " ClusterAuthInfo")
    class ClusterAuthInfo {
        @ApiModelProperty(value = " needAuthen", example = "172.16.28.123")
        private boolean needAuthen;
        @ApiModelProperty(value = "username of cluster", example = "admin")
        private String username;
        @ApiModelProperty(value = "password of tcluster", example = "admin")
        private String password;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "ClusterNodeSSHInfo", description = "ssh info including ip, username and raw password")
    class ClusterNodeSSHInfo {
        @ApiModelProperty(value = " Host ip", example = "172.16.28.123")
        private String ip;
        @ApiModelProperty(value = "username of the machine", example = "admin")
        private String username;
        @ApiModelProperty(value = "password of the machine", example = "admin")
        private String password;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "RestoreConfig", description = "BackupConfig")
    class RestoreConfig {
        @ApiModelProperty(value = " isPartionRange", example = " ")
        private boolean isPartionRange;
        @ApiModelProperty(value = " partionRangeStart", example = " ")
        private int partionRangeStart;
        @ApiModelProperty(value = " backupPartionEnd", example = " ")
        private int backupPartionEnd;
        @ApiModelProperty(value = " partionList", example = "1")
        private List<Integer> partionList;
        @ApiModelProperty(value = " restoreDirectory", example = " ")
        private String restoreDirectory;
    }
}
