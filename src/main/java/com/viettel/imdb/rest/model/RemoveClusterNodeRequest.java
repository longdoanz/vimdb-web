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
@ApiModel(value="RemoveClusterNodeRequest", description = "Request to remove a node to the current managed cluster")
public class RemoveClusterNodeRequest {
    @ApiModelProperty(value = "Host", example = "172.16.28.123")
    private String host;
    @ApiModelProperty(value = "Port", example = "10000")
    private int port;

    @ApiModelProperty(value = "Username", example = "admin")
    private String username;
    @ApiModelProperty(value = "Password", example = "admin")
    private String password;

}
