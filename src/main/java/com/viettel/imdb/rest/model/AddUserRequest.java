package com.viettel.imdb.rest.model;

import com.viettel.imdb.core.security.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
//@ApiModel(value="AddUserRequest", description = "  ")
public class AddUserRequest {
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
    private List<Role> newRoles;

    public AddUserRequest() {
    }

    public AddUserRequest(String userName, String password, List<String> roles, List<Role> newRoles) {
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
        return "AddUserRequest{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", newRoles=" + newRoles +
                '}';
    }

//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @ToString
    @ApiModel(value = "NewClusterNodeServerInfo", description = "vIMDB server configuration")
    class RoleInfo{
        @ApiModelProperty(value= "roleName", example = "")
        private String roleName;
        @ApiModelProperty(value= "privileges", example = "privileges list")
        private List<String> privileges;

        public RoleInfo() {
        }

        public RoleInfo(String roleName, List<String> privileges) {
            this.roleName = roleName;
            this.privileges = privileges;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public List<String> getPrivileges() {
            return privileges;
        }

        public void setPrivileges(List<String> privileges) {
            this.privileges = privileges;
        }

        @Override
        public String toString() {
            return "RoleInfo{" +
                    "roleName='" + roleName + '\'' +
                    ", privileges=" + privileges +
                    '}';
        }
    }


}
