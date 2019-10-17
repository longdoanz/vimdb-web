package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="InsertUDFRequest", description = "  ")
public class InsertUDFRequest {
    @ApiModelProperty(value = "fileName", example = "BalanceUpdateTrigger.lua")
    private String fileName;
    @ApiModelProperty(value = "type", example ="LUA")
    private UDFType type;
    @ApiModelProperty(value = "syncedOnAllNodes", example = "true")
    private boolean syncedOnAllNodes;
    @ApiModelProperty(value = "content", example = "")
    private String content;

//    public void setFileName(String udf_name) {
//        this.fileName = udf_name +"."+type.toString();
//    }

    public void setContent(String content) {
        this.content = content;
    }

//    public InsertUDFRequest(String udf_name, String type, boolean syncedOnAllNodes, String content) {
//        this.udf_name = udf_name;
//        this.type = type;
//        this.syncedOnAllNodes = syncedOnAllNodes;
//        this.content = content;
//    }
}
