package com.viettel.imdb.rest.restassured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeClass;

import static com.viettel.imdb.rest.common.Common.*;
import static com.viettel.imdb.rest.restassured.Constant.ROOT_URI;
import static io.restassured.RestAssured.given;

public class TestHelper {

    //    private String token = "";
    @BeforeClass
    public void beforeClass() {
        boolean needAuthorize = true;

        RequestSpecBuilder requestBuilder = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json");

        if(needAuthorize) {
            requestBuilder.addHeader("Authorization", this.authorize(USERNAME, PASSWORD));
        }
        RestAssured.requestSpecification = requestBuilder.build();

        RestAssured.baseURI = ROOT_URI;
        RestAssured.port = 8080;
    }

    public String authorize(String username, String password) {
        //language=JSON
        String body = "{\n" +
                "  \"username\": \""+ username + "\",\n" +
                "  \"password\": \""+ password+"\"\n" +
                "}";
        return "Bearer" + given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(body)
                .when()
                .post(LOGIN_PATH).then().statusCode(HttpStatus.OK.value()).toString()
                ;
    }

    public Response getRole(String... roleName) {
        String path = "";
        if(roleName.length != 0) {
            path = "/" + roleName[0];
        }
        return given()
                .when()
                .get(ROOT_URI + SECURITY_PATH + "/role" + path);
    }

    public Response createRole(String body) {
        return given()
                .body(body)
                .when()
                .post(SECURITY_PATH + "/role");
    }

    public Response updateRole(String roleName, String body) {
        return given()
                .body(body)
                .when()
                .patch(SECURITY_PATH + "/role/" + roleName);
    }

    public Response dropRole(String roleName) {
        return given()
                .when()
                .delete(SECURITY_PATH + "/role/" + roleName);
    }

    public Response createUser(String body) {
        return given()
                .body(body)
                .when()
                .post(SECURITY_PATH + "/user");
    }

    public Response getUser(String... username) {
        String path = "";
        if(username.length != 0) {
            path = "/" + username[0];
        }
        return given()
                .when()
                .get(SECURITY_PATH + "/user" + path);
    }

    public Response updateUser(String username, String body) {
        return given()
                .body(body)
                .when()
                .patch(SECURITY_PATH + "/user/" + username);
    }

    public Response dropUser(String username) {
        return given()
                .when()
                .delete(SECURITY_PATH + "/user/" + username);
    }

    public Response createUdf(String udfName, String body) {
        return given()
                .body(body)
                .when()
                .post(UDF_PATH + "/" + udfName);
    }

    public Response getUdf(String... udfName) {
        String path = "";
        if(udfName.length != 0) {
            path = "/" + udfName[0];
        }
        return given()
                .when()
                .get(UDF_PATH + path);
    }

    public Response updateUdf(String udfName, String body) {
        return given()
                .body(body)
                .when()
                .patch(UDF_PATH + "/" + udfName);
    }

    public Response dropUdf(String udfName) {
        return given()
                .when()
                .delete(UDF_PATH + "/" + udfName);
    }
}
