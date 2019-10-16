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
    @ApiModelProperty(value = " udf_name", example = "")
    private String udf_name;
    @ApiModelProperty(value = " type", example = "")
    private UDFType type;
    @ApiModelProperty(value = " syncedOnAllNodes", example = "")
    private boolean syncedOnAllNodes;
    @ApiModelProperty(value = " content", example = "")
    private String content;
    public void setName(String udf_name){
        this.udf_name = udf_name;
    }

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