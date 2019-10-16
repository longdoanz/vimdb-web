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
public class UDFInfo {
    @ApiModelProperty(value= "UDFs", example = "    ")
    private String fileName;
    @ApiModelProperty(value= "UDFType", example = "    ")
    private UDFType type;
    @ApiModelProperty(value= "createon", example = "    ")
    private long createon;
    @ApiModelProperty(value= "lastupdate", example = "    ")
    private long lastupdate;
    @ApiModelProperty(value= "content", example = "    ")
    private String content;
}
enum UDFType {
    LUA,
    ABC
}
