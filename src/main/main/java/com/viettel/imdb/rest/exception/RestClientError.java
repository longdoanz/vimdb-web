package com.viettel.imdb.rest.exception;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.ClientException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(value="RestClientError", description = "RestClientError")
public class RestClientError {
    @ApiModelProperty(value= "RestClientError", example = "404")
    private Enum error;
    private String message;

    public RestClientError(ClientException ex) {
        message = ex.getErrorCode().name();
        error = ex.getErrorCode();
    }

    public RestClientError(String msg) {
        message = msg;
        error = ErrorCode.INTERNAL_ERROR;
    }

    /*public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }*/
}
