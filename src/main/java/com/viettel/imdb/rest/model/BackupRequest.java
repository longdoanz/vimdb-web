package com.viettel.imdb.rest.model;

import com.viettel.imdb.rest.domain.RestClientError;
import com.viettel.imdb.rest.service.BackupRestoreService;
import io.swagger.annotations.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.DeferredResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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

        @ApiModelProperty(value = " isPartitionRange", example = "true")
        private boolean isPartitionRange;
        @ApiModelProperty(value = " partitionRangeStart", example = "0")
        private int partitionRangeStart;
        @ApiModelProperty(value = " backupPartitionEnd", example = "4096")
        private int backupPartitionEnd;
        @ApiModelProperty(value = " partitionList", example = "1")
        private List<Integer> partitionList;
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
