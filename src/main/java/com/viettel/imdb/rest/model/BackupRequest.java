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

        @ApiModelProperty(value = " isPartionRange", example = " ")
        private boolean isPartionRange;
        @ApiModelProperty(value = " partionRangeStart", example = " ")
        private int partionRangeStart;
        @ApiModelProperty(value = " backupPartionEnd", example = " ")
        private int backupPartionEnd;
        @ApiModelProperty(value = " partionList", example = "1")
        private List<Integer> partionList;
        @ApiModelProperty(value = " backupDirectory", example = " ")
        private String backupDirectory;

//        public BackupConfig() {
//            this.isPartionRange = true;
//            this.partionRangeStart = 0;
//            this.backupPartionEnd = 4096;
//            this.partionList = new ArrayList<>(
//                    Arrays.asList(1, 2, 3, 5, 10)
//            );
//            this.backupDirectory = "backupDirectory";
//        }
    }
}
