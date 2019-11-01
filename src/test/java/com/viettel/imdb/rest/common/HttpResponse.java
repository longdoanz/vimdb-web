package com.viettel.imdb.rest.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.viettel.imdb.util.IMDBEncodeDecoder;
import org.springframework.http.HttpStatus;
import org.testng.Assert;

import java.io.IOException;

public class HttpResponse {
    private static IMDBEncodeDecoder encoder = IMDBEncodeDecoder.getInstance();
    private HttpStatus status;
    private String response;

    ObjectMapper objectMapper = new ObjectMapper();

    public HttpResponse(int statusCode) {
        setStatus(HttpStatus.valueOf(statusCode));
    }

    public HttpResponse(int statusCode, String response) throws IOException {
        setStatus(HttpStatus.valueOf(statusCode));
        this.setResponse(response);
    }

    public HttpResponse andExpect(int expectedCode) {
        Assert.assertEquals(this.getStatus(), HttpStatus.valueOf(expectedCode), "Status code UNEXPECTED, \nRESPONSE: " + this.response + "\n");
        return this;
    }

    public HttpResponse andExpect(HttpStatus expectedHttpCode) {
        Assert.assertEquals(this.getStatus(), expectedHttpCode, "Status code UNEXPECTED, RESPONSE: " + this.response);
        return this;
    }

    public HttpResponse andExpectResponse(String expectedResponse) {
        if(expectedResponse == null) {
            Assert.assertNull(this.response);
            return this;
        }
        if(this.response == null) {
            Assert.assertNull(expectedResponse);
        }
        Assert.assertEquals(encoder.encodeJsonString(this.response), encoder.encodeJsonString(expectedResponse),
                "response UNEXPECTED, Response:\n" + this.response + "\n Expected: \n" + expectedResponse);
//        Assert.assertEquals(this.response.toString(), response, "response UNEXPECTED, RESPONSE: " + this.response);
        return this;
    }

    public <T> HttpResponse andExpectResponse(String path, T value) {
        T res = read(path);
        Assert.assertEquals(res, value, "Unexpected value \nResponse: " + this.response + "\n");
        return this;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public JsonNode getResponse() {
        try {
            return objectMapper.readTree(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    /*public HttpResponse andExpect(String jsonPath, String value) {
        Assert.assertEquals(JsonPath.parse(jsonPath), response, "response UNEXPECTED");
        return this;
    }*/

    public HttpResponse prettyPrint() {
        System.out.println(String.format("HttpStatus: %s\n" +
                "HttpResponse: %s", this.status, this.response)
        );
        return this;
    }

    public <T> T read(String path) {
        return JsonPath.parse(this.response).read(path);
    }

    public <T> T read(String path, Class<T> tClass) {
        return JsonPath.parse(this.response).read(path, tClass);
    }

}
