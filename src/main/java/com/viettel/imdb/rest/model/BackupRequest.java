package com.viettel.imdb.rest.model;

import com.viettel.imdb.rest.sshconect.AuthenticationOption;
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
    @ApiModelProperty(value= "clusterNodeSSHInfo", example = " ")
    private ClusterNodeSSHInfo backupClient;
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
    public static class ClusterAuthInfo {
        @ApiModelProperty(value = "needAuthen", example = "172.16.28.123")
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
    public static class ClusterNodeSSHInfo {
        @ApiModelProperty(value = "Host ip", example = "172.16.31.54")
        private String ip;
        @ApiModelProperty(value = "", example = "22")
        private int port;
        @ApiModelProperty(value = "username of the machine", example = "imdb")
        private String username;
        @ApiModelProperty(value = "AuthenticationOption", example = "none")
        private AuthenticationOption authenticationOption;
        @ApiModelProperty(value = "password of the machine", example = "imdb")
        private String password;
        @ApiModelProperty(value = "password of the machine", example = "admiz")
        private String sshKey;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "BackupConfig", description = "BackupConfig")
    public static class BackupConfig {

        @ApiModelProperty(value = " partitionRangeStart", example = "true")
        private boolean isPartitionRange;
        @ApiModelProperty(value = " partionRangeStart", example = "0")
        private int partitionRangeStart;
        @ApiModelProperty(value = " backupPartionEnd", example = "4095")
        private int partitionRangeEnd;
        @ApiModelProperty(value = " partionList", example = "[1, 10, 20, 12, 123, 2]")
        private List<Integer> parittionList;
        @ApiModelProperty(value = " backupDirectory", example = "/home/imdb/backup/")
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
