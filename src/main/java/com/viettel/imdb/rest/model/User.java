package com.viettel.imdb.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viettel.imdb.core.security.Role;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private String password;
    private List<Privilege> privileges;
    private List<String> parentRoles;


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

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    public List<String> getParentRoles() {
        return parentRoles;
    }


    public void setParentRoles(List<String> parentRoles) {
        this.parentRoles = parentRoles;
    }
}
