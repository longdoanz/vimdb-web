package com.viettel.imdb.rest.restassured;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.viettel.imdb.rest.restassured.Constant.ROOT_URI;
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

    @Test
    public void testCreateTable() {
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"name\": \"Lisa Tamaki\",\"salary\": \"20000\"}")
                .when()
                .put("/update/3");
        System.out.println("PUT Response\n" + response.asString());
        // tests
        response.then().body("id", Matchers.is(3));
        response.then().body("name", Matchers.is("Lisa Tamaki"));
        response.then().body("salary", Matchers.is("20000"));
    }
}
