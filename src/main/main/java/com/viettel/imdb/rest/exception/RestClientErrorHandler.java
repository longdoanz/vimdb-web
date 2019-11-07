package com.viettel.imdb.rest.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.common.ClientException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestClientErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ClientException.class})
    public ResponseEntity<Object> handleClientException(ClientException ex) {
        return new ResponseEntity<>(new RestClientError(ex), getStatusCodeFromException(ex));
    }


    @NotNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.warn(ex.getMessage());
        return new ResponseEntity<>(new RestClientError(ex.getMostSpecificCause().getMessage()), status);
    }

    @ExceptionHandler({ExceptionType.VIMDBRestClientError.class})
    public ResponseEntity<Object> handleAPIError(ExceptionType.VIMDBRestClientError ex) {
        return new ResponseEntity<>(new RestClientError(ex.getErrorCode(), ex.getMessage()), ex.getStatusCode());
    }

    @ExceptionHandler({JsonMappingException.class})
    public ResponseEntity<Object> handleJSONMappingException(JsonMappingException jme) {
        return new ResponseEntity<>(new RestClientError(jme.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private HttpStatus getStatusCodeFromException(ClientException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        switch (errorCode) {
            case TABLE_EXIST:
            case TABLENAME_LENGTH_INVALID:
            case TABLENAME_INVALID:
            case TABLE_OPERATION_TIMEOUT:
            case TABLE_IN_USE:
            case KEY_INVALID:
            case KEY_LENGTH_INVALID:
            case SEC_INDEX_NOT_EXIST:
            case KEY_EXIST:
            case CONFLICT_OPERATION_ON_KEY:
            case SEC_INDEX_EXIST:
            case FIELDNAME_INVALID:
            case FIELDNAME_LENGTH_INVALID:
                return HttpStatus.BAD_REQUEST;
            case TABLE_NOT_EXIST:
            case KEY_NOT_EXIST:
                return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
