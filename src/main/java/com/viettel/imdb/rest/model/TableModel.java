package com.viettel.imdb.rest.model;

import com.viettel.imdb.rest.RestErrorCode;

/**
 * @author longdt20
 * @since 11:01 05/12/2018
 */

public class TableModel {
    private String db;
    private String tableName;
//    public String encode;

    public TableModel() {
    }

    public TableModel(String db, String tableName) {
        this.db = db;
        this.tableName = tableName;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public RestErrorCode validateData() {
        if (db == null || db.isEmpty() || !db.equals("default")) {
            return RestErrorCode.DATABASE_NOT_EXIST;
        }
        if (tableName == null || tableName.isEmpty()) {
            return RestErrorCode.TABLENAME_LENGTH_INVALID;
        }
        return RestErrorCode.OK;
    }

    public String getTableName() {
        return this.tableName;
    }

    @Override
    public String toString() {
        return "TableModel{name: " + this.tableName + "}";
    }
}
