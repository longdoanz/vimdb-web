package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModelProperty;

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
//@ApiModel(value="RoleInfo", description = "Role info")
public class RoleInfo {
    @ApiModelProperty(value= "roleName", example = "admin")
    private String roleName;
    @ApiModelProperty(value= "privileges", example = "[]")
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
