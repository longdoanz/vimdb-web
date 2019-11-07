package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@ApiModel(value = "CmdRequest")
public class CmdRequest {
    @ApiModelProperty(value = "cmd", example = "SELECT * FROM TABLE_01;")
    private String cmd;
}
