package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="ProcessStatus", description = "Process Status: processing,failed, completed\n")
public class ProcessStatus {
    @ApiModelProperty(value= "callback", example = "Process Status")
    private String status;
}
