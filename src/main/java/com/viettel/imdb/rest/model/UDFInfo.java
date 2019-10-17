package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="UDFInfo", description = "  ")
public class UDFInfo {
    @ApiModelProperty(value= "UDFs", example = "CustInfoUpdateTrigger.lua")
    private String fileName;
    @ApiModelProperty(value= "UDFType", example = "LUA")
    private UDFType type;
    @ApiModelProperty(value= "synced On All Nodes", example = "true")
    private boolean syncedOnAllNodes;
    @ApiModelProperty(value= "createon", example = "324325324")
    private long createon;
    @ApiModelProperty(value= "lastupdate", example = "324325324")
    private long lastupdate;
    @ApiModelProperty(value= "content", example = "VERY LONG TO DISPLAY HERE")
    private String content;

    public String udf_name(){
        int iend = fileName.indexOf(".");
        if (iend != -1){
            return fileName.substring(0, iend);
        }else {
            return fileName;
        }
    }
}
enum UDFType {
    LUA,
    ABC
}
