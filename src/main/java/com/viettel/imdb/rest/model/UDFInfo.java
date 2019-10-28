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
    private String name;
    @ApiModelProperty(value= "UDFType", example = "LUA")
    private UDFType type;
    @ApiModelProperty(value= "synced On All Nodes", example = "true")
    private boolean syncedOnAllNodes;
    @ApiModelProperty(value= "createon", example = "324325324")
    private long createdOn;
    @ApiModelProperty(value= "lastupdate", example = "324325324")
    private long lastUpdate;
    @ApiModelProperty(value= "content", example = "VERY LONG TO DISPLAY HERE")
    private String content;

}
enum UDFType {
    LUA,
    ABC
}
