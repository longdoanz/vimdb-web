package com.viettel.imdb.rest.model;

import com.viettel.imdb.rest.RestErrorCode;

/**
 * @author longdt20
 * @since 11:01 05/12/2018
 */

public class TableModel {
    private String db;
    private String name;
//    public String encode;

    public TableModel() {
    }

    public TableModel(String db, String name) {
        this.db = db;
        this.name = name;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public RestErrorCode validateData() {
        if (db == null || db.isEmpty() || !db.equals("default")) {
            return RestErrorCode.DATABASE_NOT_EXIST;
        }
        if (name == null || name.isEmpty()) {
            return RestErrorCode.TABLENAME_LENGTH_INVALID;
        }
        return RestErrorCode.OK;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "TableModel{name: " + this.name + "}";
    }
}
