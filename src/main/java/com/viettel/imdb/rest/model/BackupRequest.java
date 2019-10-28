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
@ApiModel(value="BackupRequest", description = "  ")
public class BackupRequest {
    @ApiModelProperty(value= "clusterAuthInfo", example = " ")
    private ClusterAuthInfo clusterAuthInfo;
    @ApiModelProperty(value= "clusterNodeSSHInfo", example = " ")
    private ClusterNodeSSHInfo clusterNodeSSHInfo;
    @ApiModelProperty(value= "backupConfig", example = " ")
    private BackupConfig backupConfig;

//    public BackupRequest() {
//        this.clusterAuthInfo = new ClusterAuthInfo();
//        this.clusterNodeSSHInfo = new ClusterNodeSSHInfo();
//        this.backupConfig = new BackupConfig();
//    }

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
    @ApiModel(value = "BackupConfig", description = "BackupConfig")
    class BackupConfig {

        @ApiModelProperty(value = "isPartitionRange", example = " ")
        private boolean isPartitionRange;
        @ApiModelProperty(value = "partitionRangeStart", example = " ")
        private int partitionRangeStart;
        @ApiModelProperty(value = "backupPartitionEnd", example = " ")
        private int backupPartitionEnd;
        @ApiModelProperty(value = "partitionList", example = "[]")
        private List<Integer> partitionList;
        @ApiModelProperty(value = "backupDirectory", example = " ")
        private String backupDirectory;

//        public BackupConfig() {
//            this.isPartitionRange = true;
//            this.partitionRangeStart = 0;
//            this.backupPartitionEnd = 4096;
//            this.partitionList = new ArrayList<>(
//                    Arrays.asList(1, 2, 3, 5, 10)
//            );
//            this.backupDirectory = "backupDirectory";
//        }
    }
}
