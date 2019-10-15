package com.viettel.imdb.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.imdb.common.ValueType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * @author quannh22
 * @since 09/08/2019
 */

@ApiModel
public class RestIndexModel {
    /* convert an info call to a RestIndexModel */
    @ApiModelProperty(hidden=true)
    private static final String NAMESPACE = "namespace";
    @ApiModelProperty(hidden=true)
    private static final String TABLE = "table";
    @ApiModelProperty(hidden=true)
    private static final String TYPE = "type";
    @ApiModelProperty(hidden=true)
    private static final String NAME = "name";

    @ApiModelProperty(value = "namespace name, default value = NAMESPACE", example = "NAMESPACE")
    private String namespace;

    @ApiModelProperty(value = "table name", example = "tablename")
    private String table;

    @JsonProperty("type")
    private ValueType type;

    @ApiModelProperty(value = "index name ", example = "$.custId")
    private String name;

    public RestIndexModel() {
    }

    public RestIndexModel(Map<String, String> inputParams) {
        this.namespace = inputParams.get(NAMESPACE);
        this.table = inputParams.get(TABLE);
        this.type = getIndexType(inputParams.get(TYPE));
        this.name = inputParams.get(NAME);
    }

    public RestIndexModel(String namespace, String table, String type, String name) {
        this.namespace = namespace;
        this.table = table;
        this.type = getIndexType(name);
        this.name = name;
    }

    private ValueType getIndexType(String indexTypeStr) {
        switch (indexTypeStr) {
            case "string":
                return ValueType.STRING;
            case "numeric":
            default:
                return ValueType.NUMERIC;
        }
    }


    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toJsonString() {
        try {
            ObjectMapper om = new ObjectMapper();
            String re =  om.writeValueAsString(this);
            System.out.println(re);
            return re;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    public String toString() {
        return "RestIndexModel{" +
                "namespace='" + namespace + '\'' +
                ", table='" + table + '\'' +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
