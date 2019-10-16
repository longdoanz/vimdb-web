package com.viettel.imdb.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="CallbackResponse", description = "Callback Response")
public class CallbackResponse {
    @ApiModelProperty(value= "callback", example = "callback check api")
    private String callback;
}
