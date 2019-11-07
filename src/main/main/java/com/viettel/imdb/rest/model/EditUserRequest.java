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
    @ApiModelProperty(value= "password", example = "admin13")
    private String password;
    @ApiModelProperty(value= "roles", example = "[\n" +
            "    \"read-write.data.CustInfo\",\n" +
            "    \"read-write.data.MappingSubCust\"\n" +
            "  ]\n")
    private List<String> roles;
    @ApiModelProperty(value= "newRoles", example = "[\n" +
            "    {\n" +
            "      \"name\": \"read-write.data.SessionSREData\",\n" +
            "      \"privileges\": [\n" +
            "       {\n" +
            "         \"permission\": \"read\",\n" +
            "         \"resource\": {\n" +
            "           \"name\": \"data\",\n" +
            "           \"namespace\": \"NAMESPACE\",\n" +
            "           \"table\": \"table01\"\n" +
            "         }\n" +
            "       }  \n" +
            "     ]\n" +
            "    }\n" +
            "  ]\n")
    private List<Role> newRoles; // todo NOT HERE

    public EditUserRequest() {
    }

    public EditUserRequest(String password, List<String> roles, List<Role> newRoles) {
        this.password = password;
        this.roles = roles;
        this.newRoles = newRoles;
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
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", newRoles=" + newRoles +
                '}';
    }
}
