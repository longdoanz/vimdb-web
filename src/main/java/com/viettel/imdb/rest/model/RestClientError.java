package com.viettel.imdb.rest.model;

import com.viettel.imdb.rest.RestErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="RestClientError", description = "RestClientError")
public class RestClientError {
    @ApiModelProperty(value= "RestClientError", example = "404")
    private RestErrorCode error;
    private String message;

    /*public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }*/
}
