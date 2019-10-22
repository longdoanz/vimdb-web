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
@ApiModel(value="AddClusterNodeRequest", description = "Request to add a node to the current managed cluster")
public class AddClusterNodeRequest {
    @ApiModelProperty(value= "SSHInfo of the server. Should change to SSH key in future")
    private ClusterNodeSSHInfo sshInfo;
    @ApiModelProperty(value = "vIMDB server cluster config")
    private NewClusterNodeServerInfo vimdbServerInfo;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "ClusterNodeSSHInfo", description = "ssh info including ip, username and raw password")
    class ClusterNodeSSHInfo {
        @ApiModelProperty(value = "Host ip", example = "172.16.28.123")
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
    @ApiModel(value = "NewClusterNodeServerInfo", description = "vIMDB server configuration")
    public
    class NewClusterNodeServerInfo {
        @ApiModelProperty(value = "Server host", example = "172.16.28.123:12345")
        private String host;
        @ApiModelProperty(value = "Server Port", example = "10000")
        private int port;

        @ApiModelProperty(value = "Path of vIMDB runnable binary", example = "/home/imdb/bin/vimdb")
        private String binaryPath;
        @ApiModelProperty(value = "Configuration file path", example = "/home/imdb/conf/conf.toml")
        private String configFilePath;
        @ApiModelProperty(value = "Override setting or not", example = "true")
        private boolean overridedSetting;
        @ApiModelProperty(value = "reload old data or not", example = "true")
        private boolean reloadOldData;
        @ApiModelProperty(value = "Cluster authentication info")
        private ClusterAuthenInfo authenInfo;
        @ApiModelProperty(value = "ClusterNodeOverridedConfig info")
        private ClusterNodeOverridedConfig overridedConfig;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "ClusterAuthenInfo", description = "Cluster authentication info")
    class ClusterAuthenInfo {
        @ApiModelProperty(value = "Need authen or not", example = "true")
        private boolean needAuthen;
        @ApiModelProperty(value = "Cluster username", example = "admin")
        private String username;
        @ApiModelProperty(value = "Cluster password", example = "admin")
        private String password;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "ClusterOverridedCOnfig", description = "Override the whole config")
    class ClusterNodeOverridedConfig {
        @ApiModelProperty(value = "Overrided or not", example = "true")
        private boolean overrided;
        @ApiModelProperty(value = "Config file name", example = "config.toml")
        private String configFile;
        @ApiModelProperty(value = "New configuration content", example = "TOO LONG HERE")
        private String newConfigContent;
    }
}
