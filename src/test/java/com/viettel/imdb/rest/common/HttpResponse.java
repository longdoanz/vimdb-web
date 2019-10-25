package com.viettel.imdb.rest.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.HttpStatus;
import org.testng.Assert;

import java.io.IOException;

public class HttpResponse {
    public HttpStatus status;
    public JsonNode response;

    ObjectMapper objectMapper = new ObjectMapper();

    public HttpResponse(int statusCode, String response) throws IOException {
        status = HttpStatus.valueOf(statusCode);
        this.response = objectMapper.readTree(response);
    }

    public HttpResponse andExpect(int statusCode) {
        Assert.assertEquals(this.status, HttpStatus.valueOf(statusCode), "Status code UNEXPECTED");
        return this;
    }

    public HttpResponse andExpect(String response) {
        Assert.assertEquals(response, response, "response UNEXPECTED");
        return this;
    }

    /*public HttpResponse andExpect(String jsonPath, String value) {
        Assert.assertEquals(JsonPath.parse(jsonPath), response, "response UNEXPECTED");
        return this;
    }*/

}
