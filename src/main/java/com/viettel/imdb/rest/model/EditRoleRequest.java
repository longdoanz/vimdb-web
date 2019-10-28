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
    @ApiModelProperty(value = " privileges", example = "")
    private List<String> privileges;

    public EditRoleRequest() {
    }

    public EditRoleRequest(List<String> privileges) {
        this.privileges = privileges;
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
                ", privileges=" + privileges +
                '}';
    }
}
