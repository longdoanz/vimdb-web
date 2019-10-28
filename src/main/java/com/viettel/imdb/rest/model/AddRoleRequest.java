package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author quannh22
 * @since 18/09/2019
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
//@ApiModel(value="AddRoleRequest", description = "  ")
public class AddRoleRequest {
    @ApiModelProperty(value= "roleName", example = " ")
    private String roleName;
    @ApiModelProperty(value= "privileges", example = " ")
    private List<String> privileges;

/*
    public AddRoleRequest() {
    }

    public AddRoleRequest(String roleName, List<String> privileges) {
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
        return "AddRoleRequest{" +
                "roleName='" + roleName + '\'' +
                ", privileges=" + privileges +
                '}';
    }
*/
    @Getter
    @Setter
    @ToString
    public static class Privilege {
        String permission;
        static class Resource {
            String name;
            String user;
            String namespace;
            String table;
        }
    }
}
