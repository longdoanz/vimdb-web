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
    @ApiModelProperty(value= "ClusterNodeSSHInfo", example = "ClusterNodeSSHInfo")
    private ClusterNodeSSHInfo sshInfo;
    @ApiModelProperty(value = "NewClusterNodeServerInfo", example = "TOO LONG TO DESCRIBE HERE")
    private NewClusterNodeServerInfo vimdbServerInfo;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "ClusterNodeSSHInfo", description = "ssh info including ip, username and raw password")
    public static class ClusterNodeSSHInfo {
        @ApiModelProperty(value = "Host ip", example = "172.16.28.123")
        private String ip;
        @ApiModelProperty(value = "Port", example = "22")
        private int port;

        @ApiModelProperty(value = "Authentication Option", example = "none/password/sshkey")
        private String authenticationOption;

        @ApiModelProperty(value = "username of the machine", example = "imdb")
        private String username;
        @ApiModelProperty(value = "password of the machine", example = "imdb")
        private String password;
        @ApiModelProperty(value = "SSHKey for login", example = "xxxxxMIIEpxxxxx")
        private String sshkey;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "NewClusterNodeServerInfo", description = "vIMDB server configuration")
    public static
    class NewClusterNodeServerInfo {
        @ApiModelProperty(value = "Server host", example = "172.16.28.123")
        private String host;
        @ApiModelProperty(value = "Server Port", example = "22")
        private int port;

        @ApiModelProperty(value = "Binary file option", example = "false")
        private boolean defaultBinary;
        @ApiModelProperty(value = "Path of vIMDB runnable binary", example = "/home/imdb/bin/vimdb")
        private String binary;

        @ApiModelProperty(value = "Binary file option", example = "false")
        private boolean defaultConfigFile;
        @ApiModelProperty(value = "Configuration file path", example = "/home/imdb/conf/conf.toml")
        private String configFile;
        /*@ApiModelProperty(value = "reload old data or not", example = "true")
        private boolean reloadOldData;*/
    }

}
