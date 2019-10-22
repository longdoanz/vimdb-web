package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="AuthenRespone", description = "Request")
public class AuthenRespone {
    @ApiModelProperty(value= "token", example = "admin-admin")
    private String jwttoken;

//    public AuthenRespone(String jwttoken) {
//        this.jwttoken = jwttoken;
//    }

//    public String getToken() {
//        return this.jwttoken;
//    }
}
