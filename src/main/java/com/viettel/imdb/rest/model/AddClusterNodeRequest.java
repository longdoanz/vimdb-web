package com.viettel.imdb.rest.model;
import com.viettel.imdb.rest.sshconect.AuthenticationOption;
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
        @ApiModelProperty(value = "username of the machine", example = "imdb")
        private String username;
        @ApiModelProperty(value = "AuthenticationOption", example = "none")
        private AuthenticationOption authenticationOption;
        @ApiModelProperty(value = "password of the machine", example = "imdb")
        private String password;
        @ApiModelProperty(value = "private Key", example = "admind")
        private String sshKey;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @ApiModel(value = "NewClusterNodeServerInfo", description = "vIMDB server configuration")
    public
    static class NewClusterNodeServerInfo {
        @ApiModelProperty(value = "boolean", example = "false")
        boolean defaultBinary;
        @ApiModelProperty(value = "Path of vIMDB runnable binary", example = "/home/imdb/longdt/vimdb2/release/apps/vimdb_with_security")
        private String binary;
        @ApiModelProperty(value = "boolean", example = "false")
        boolean defaultConfigFile;
        @ApiModelProperty(value = "Configuration file path", example = "/home/imdb/longdt/vimdb2/release/apps/vimdb_thurvnode0.toml")
        private String configFile;

        @ApiModelProperty(value = "Config conntent", example = "cluster_name = \"vIMDB Cluster\"\n" +
                "host = \"172.16.28.123\"\n" +
                "port = 10000\n" +
                "replication_factor = 3\n" +
                "metric_port = 11111\n" +
                "seeds = [\n" +
                "  \"172.16.28.123:29050\"\n" +
                "  \"172.16.28.123:29051\"\n" +
                "  \"172.16.28.123:14080\"\n" +
                "]\n" +
                "default_log_level = \"info\"\n" +
                "cluster_backup_log = \"thurv_cluster0.log\"\n" +
                "backup_data_directory = \"thurv_db0\"\n" +
                "[log_level]\n" +
                "vimdb = \"error\"")
        private String newConfigContent;
    }

}
