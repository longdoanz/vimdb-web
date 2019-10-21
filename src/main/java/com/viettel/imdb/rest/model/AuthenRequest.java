package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="AddClusterNodeRequest", description = "Request to add a node to the current managed cluster")
public class AuthenRequest {
    @ApiModelProperty(value= "username", example = "admin")
    private String username;
    @ApiModelProperty(value= "password", example = "admin")
    private String password;
}
