package com.viettel.imdb.rest.domain;

import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.rest.exception.ExceptionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author quannh22
 * @since 08/08/2019
 */
@ApiModel(value="RestClientError", description = "Error object returned from unsuccessful operations. Work for swagger")
public class RestClientError {
    @ApiModelProperty(
            value= "A message describing the cause of error.",
            example = "Error message")
    private String message;

    @ApiModelProperty(
            value = "A boolean specifying whether it was possible that the operation succeeded. This is only included if true.",
            required = false,
            example = "false"
    )
    private boolean inDoubt;

    @ApiModelProperty(
            value = "An internal error code for diagnostic purpose. This may be null",
            example = "100"
    )
    private Short internalErrorCode;

    public RestClientError(ClientException ex) {
        this.message = ex.getMessage();
        this.inDoubt = false;
        this.internalErrorCode = ex.getErrorCode().get();
    }

    public RestClientError(ExceptionType.VIMDBRestClientError ex) {
        this.message = ex.getMessage();
        this.inDoubt = false;
        this.internalErrorCode = null;
    }

    public RestClientError(String message) {
        this.message = message;
        this.inDoubt = false;
        this.internalErrorCode = null;
    }

    public String getMessage() {
        return message;
    }

    public boolean isInDoubt() {
        return inDoubt;
    }

    public short getInternalErrorCode() {
        return internalErrorCode;
    }
}
