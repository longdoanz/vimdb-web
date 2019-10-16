package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author quannh22
 * @since 15/10/2019
 */
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
//@ApiModel(value="UserInfo", description = "User info")
public class UserInfo {
    @ApiModelProperty(value= "username", example = "admin")
    private String username;
    @ApiModelProperty(value= "password", example = "admin")
    private String password;
    @ApiModelProperty(value= "roles", example = "[]")
    private List<String> roles;

    public UserInfo(String username) {
        this.username = username;
    }

    public UserInfo(String username, String password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
