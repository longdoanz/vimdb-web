package com.viettel.imdb.rest.auto;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.viettel.imdb.rest.auto.Constant.ROOT_URI;
import static io.restassured.RestAssured.given;

public class DataTest {

    @BeforeClass
    public void beforeClass() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        RestAssured.requestSpecification = requestSpecification;
        RestAssured.baseURI = ROOT_URI;
        RestAssured.port = 8080;
    }

}
