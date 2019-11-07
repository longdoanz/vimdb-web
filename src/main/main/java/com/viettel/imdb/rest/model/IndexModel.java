package com.viettel.imdb.rest.model;

import com.viettel.imdb.common.ValueType;

/**
 * @author longdt20
 * @since 18:00 20/02/2019
 */

public class IndexModel {
    private String name;
    private ValueType type;

    public IndexModel() {
    }

    public IndexModel(String fieldName) {
        setName(fieldName);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name.startsWith("$.")) {
            this.name = name;
        } else {
            this.name = "$." + name;
        }
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public ValueType getType() {
        return this.type == null ? ValueType.NUMERIC : this.type;
    }

    public String toString() {
        return "IndexModel: {name: " + this.name + ", type: " + this.type + "}";
    }
}
