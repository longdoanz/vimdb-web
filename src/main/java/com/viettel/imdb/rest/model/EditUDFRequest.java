package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="EditUDFRequest", description = "  ")
public class EditUDFRequest {
    @ApiModelProperty(value = "fileName", example = "")
    private String fileName;
    @ApiModelProperty(value = " type", example = "")
    private UDFType type;
    @ApiModelProperty(value = " syncedOnAllNodes", example = "")
    private boolean syncedOnAllNodes;
    @ApiModelProperty(value = " content", example = "")
    private String content;
//    public void setName(String udf_name){
//        this.fileName = udf_name +"."+type;
//    }



//    public EditUDFRequest(String udf_name, String type, boolean syncedOnAllNodes, String content) {
//        this.udf_name = udf_name;


//        this.type = type;
//        this.syncedOnAllNodes = syncedOnAllNodes;
//        this.content = content;
//    }


    public void setContent(String content) {
        this.content = content;
    }
}
