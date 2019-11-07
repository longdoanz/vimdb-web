package com.viettel.imdb.rest.model;

import java.util.List;

public class Privilege {

    private PrivilegeType type;
    private String dbName;
    private List<String> tables;

    public PrivilegeType getType() {
        return type;
    }

    public void setType(PrivilegeType type) {
        this.type = type;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }
}
