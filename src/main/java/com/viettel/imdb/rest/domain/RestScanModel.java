package com.viettel.imdb.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.imdb.common.ValueType;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * @author quannh22
 * @since 09/08/2019
 */
public class RestScanModel {
    /* convert an info call to a RestIndexModel */
    @ApiModelProperty(hidden=true)
    private static final String NAMESPACE = "namespace";
    @ApiModelProperty(hidden=true)
    private static final String TABLE = "table";
    @ApiModelProperty(hidden=true)
    private static final String TYPE = "type";
    @ApiModelProperty(hidden=true)
    private static final String NAME = "name";
    @ApiModelProperty(hidden=true)
    private static final String MIN = "min";
    @ApiModelProperty(hidden=true)
    private static final String MAX = "max";

    @ApiModelProperty(value = "namespace name, default value = NAMESPACE", example = "NAMESPACE")
    private String namespace;

    @ApiModelProperty(value = "table name", example = "tablename")
    private String table;

    @JsonProperty("type")
    private ValueType type;

    @ApiModelProperty(value = "index name ", example = "$.custId")
    private String name;

    @ApiModelProperty(value = "min value", example = "10")
    private long min;

    @ApiModelProperty(value = "max value", example = "100")
    private long max;

    public RestScanModel() {
    }

    public RestScanModel(Map<String, String> inputParams) {
        this.namespace = inputParams.get(NAMESPACE);
        this.table = inputParams.get(TABLE);
        this.type = getIndexType(inputParams.get(TYPE));
        this.name = inputParams.get(NAME);
        this.min = Long.parseLong(inputParams.get(MIN)); // todo invalid-error here
        this.max = Long.parseLong(inputParams.get(MAX)); // todo invalid-error here
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

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }
}
