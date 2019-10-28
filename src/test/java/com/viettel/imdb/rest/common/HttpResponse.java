package com.viettel.imdb.rest.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.testng.Assert;

import java.io.IOException;

public class HttpResponse {
    private HttpStatus status;
    private JsonNode response;

    ObjectMapper objectMapper = new ObjectMapper();

    public HttpResponse(int statusCode) {
        setStatus(HttpStatus.valueOf(statusCode));
    }

    public HttpResponse(int statusCode, String response) throws IOException {
        setStatus(HttpStatus.valueOf(statusCode));
        this.setResponse(objectMapper.readTree(response));
    }

    public HttpResponse andExpect(int expectedCode) {
        Assert.assertEquals(this.getStatus(), HttpStatus.valueOf(expectedCode), "Status code UNEXPECTED");
        return this;
    }

    public HttpResponse andExpect(HttpStatus expectedHttpCode) {
        Assert.assertEquals(this.getStatus(), expectedHttpCode, "Status code UNEXPECTED");
        return this;
    }

    public HttpResponse andExpectResponse(String response) {
        Assert.assertEquals(response, response, "response UNEXPECTED");
        return this;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public JsonNode getResponse() {
        return response;
    }

    public void setResponse(JsonNode response) {
        this.response = response;
    }

    /*public HttpResponse andExpect(String jsonPath, String value) {
        Assert.assertEquals(JsonPath.parse(jsonPath), response, "response UNEXPECTED");
        return this;
    }*/

}
