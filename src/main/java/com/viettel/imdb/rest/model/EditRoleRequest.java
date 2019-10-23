package com.viettel.imdb.rest.model;

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
//@ApiModel(value="EditRoleRequest", description = "  ")
public class EditRoleRequest {
    @ApiModelProperty(value = " rolename", example = "")
    private String roleName;
    @ApiModelProperty(value = " privileges", example = "")
    private List<String> privileges;

    public EditRoleRequest() {
    }

    public EditRoleRequest(String roleName, List<String> privileges) {
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
        return "EditRoleRequest{" +
                "roleName='" + roleName + '\'' +
                ", privileges=" + privileges +
                '}';
    }
}
