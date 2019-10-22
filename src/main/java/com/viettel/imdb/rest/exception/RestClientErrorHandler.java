package com.viettel.imdb.rest.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
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
    public ResponseEntity<Object> handleVIMDBError(ClientException ex) {
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
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
