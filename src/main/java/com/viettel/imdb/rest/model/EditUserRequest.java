package com.viettel.imdb.rest.model;

import com.viettel.imdb.core.security.Role;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author quannh22
 * @since 18/09/2019
 */
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
//@ApiModel(value="EditUserRequest", description = "  ")
public class EditUserRequest {

    @ApiModelProperty(value= "username", example = "admin13")
    private String userName;
    @ApiModelProperty(value= "password", example = "admin13")
    private String password;
    @ApiModelProperty(value= "roles", example = "[\n" +
            "    \"read-write.data.CustInfo\",\n" +
            "    \"read-write.data.MappingSubCust\"\n" +
            "  ]\n")
    private List<String> roles;
    @ApiModelProperty(value= "newRoles", example = "")
    private List<Role> newRoles; // todo NOT HERE

    public EditUserRequest() {
    }

    public EditUserRequest(String userName, String password, List<String> roles, List<Role> newRoles) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
        this.newRoles = newRoles;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public List<Role> getNewRoles() {
        return newRoles;
    }

    public void setNewRoles(List<Role> newRoles) {
        this.newRoles = newRoles;
    }

    @Override
    public String toString() {
        return "EditUserRequest{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", newRoles=" + newRoles +
                '}';
    }
}
