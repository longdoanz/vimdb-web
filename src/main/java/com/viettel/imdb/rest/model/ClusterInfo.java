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
@ApiModel(value="ClusterInfo", description = "Cluster info")
public class ClusterInfo {
    @ApiModelProperty(value= "nodeCount", example = "4")
    private int nodeCount;
    @ApiModelProperty(value= "replicationFactor", example = "2")
    private int replicationFactor;
    @ApiModelProperty(value= "Sversion", example = "1")
    private String version;
    @ApiModelProperty(value= "uptime", example = "36000")
    private int uptime;
    @ApiModelProperty(value= "monitorNodeCount", example = "2")
    private long monitorNodeCount;
    @ApiModelProperty(value= "ClusterDRInfo")
    private ClusterDRInfo drInfo;
    @ApiModelProperty(value= "ClusterNodeInfo")
    private List<ClusterNodeInfo> nodes;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value="ClusterInfo", description = "Cluster info")
    public class ClusterDRInfo{
        @ApiModelProperty(value= "db ", example = " ")
        private String db;
        @ApiModelProperty(value= "method", example = " ")
        private String method;
        @ApiModelProperty(value= "totalOps", example = "2")
        private long totalOps;
        @ApiModelProperty(value= "opsRead", example = "2")
        private long opsRead;
        @ApiModelProperty(value= "opsTransferred", example = "3")
        private long opsTransferred;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value="ClusterInfo", description = "Cluster info")
    public class ClusterNodeInfo{
        @ApiModelProperty(value= "name", example = "abz")
        private String name;
        @ApiModelProperty(value= "ip", example = "12.1234.346")
        private String ip;
        @ApiModelProperty(value= "port", example = "10000")
        private int port;
        @ApiModelProperty(value= "version", example = "1.0.0.0")
        private String version;
        @ApiModelProperty(value= "uptime", example = "1030")
        private long uptime;
        @ApiModelProperty(value= "os", example = " ")
        private String os;
        @ApiModelProperty(value= "heartbeatSent", example = "100")
        private long heartbeatSent;
        @ApiModelProperty(value= "hearbeatReceived", example = "1000")
        private long hearbeatReceived;
        @ApiModelProperty(value= "ramUsagePercentage", example = "10.0")
        private float ramUsagePercentage;
        @ApiModelProperty(value= "diskUsagePercentage", example = "10.2")
        private float diskUsagePercentage;
        @ApiModelProperty(value= "ClusterNodeDataInfo")
        private ClusterNodeDataInfo data;
        @ApiModelProperty(value= "ClusterNodePerformanceInfo")
        private ClusterNodePerformanceInfo performance;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value="ClusterNodeDataInfo", description = "Cluster info")
    public class ClusterNodeDataInfo{
        @ApiModelProperty(value= "tables", example = "1000")
        private long tables;
        @ApiModelProperty(value= "records", example = "1000")
        private long records;
        @ApiModelProperty(value= "master", example = "1000")
        private long master;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value="ClusterNodePerformanceInfo", description = "Cluster Node Performanceinfo")
    public class ClusterNodePerformanceInfo{
        @ApiModelProperty(value= "write", example = "1000")
        private long write;
        @ApiModelProperty(value= "writeSuccess", example = "1000")
        private long writeSuccess;
        @ApiModelProperty(value= "read", example = "1000")
        private long read;
        @ApiModelProperty(value= "readSuccess", example = "1000")
        private long readSuccess;
        @ApiModelProperty(value= "delete", example = "1000")
        private long delete;
        @ApiModelProperty(value= "deleteSuccess", example = "1000")
        private long deleteSuccess;
        @ApiModelProperty(value= "scan", example = "1000")
        private long scan;
        @ApiModelProperty(value= "scanSuccess", example = "1000")
        private long scanSuccess;

    }
}
