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
    @ApiModelProperty(value = "clusterNodeSSHInfo", example = "")
    private ClusterNodeSSHInfo restoreClient;
    @ApiModelProperty(value = "restoreConfig", example = "")
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
        @ApiModelProperty(value = "password of tcluster", example = "1")
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
        private String host;
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
    @ApiModel(value = "RestoreConfig", description = "RestoreConfig")
    class RestoreConfig {
        @ApiModelProperty(value = "isPartionRange", example = "true")
        private boolean isPartitionRange;
        @ApiModelProperty(value = "partionRangeStart", example = "4095")
        private int partionRangeStart;
        @ApiModelProperty(value = "backupPartionEnd", example = " ")
        private int backupPartionEnd;
        @ApiModelProperty(value = "partionList", example = "[1, 10, 20, 12, 123, 2, 1, 2]")
        private List<Integer> parittionList;
        @ApiModelProperty(value = "restoreDirectory", example = "/home/imdb/backup/")
        private String restoreDirectory;
    }
}
