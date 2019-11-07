package com.viettel.imdb.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PrivilegeType {

    READ, READ_WRITE, DATA_ADMIN, SYS_ADMIN, USER_ADMIN, SUPER_ADMIN;

    @JsonCreator
    public static PrivilegeType fromNode(String node) {
        if (node == null)
            return null;

        String name = node.toUpperCase();

        return PrivilegeType.valueOf(name);
    }

    @JsonProperty
    public String getName() {
        return name().toLowerCase();
    }

}
