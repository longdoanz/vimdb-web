package com.viettel.imdb.rest.auto;

import com.viettel.imdb.rest.common.TestUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import static com.viettel.imdb.rest.auto.Constant.ROOT_URI;
import static com.viettel.imdb.rest.common.Common.SECURITY_PATH;
import static com.viettel.imdb.rest.common.Common.UDF_PATH;
import static io.restassured.RestAssured.given;

public class TestHelper {

    @BeforeClass
    public void beforeClass() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        RestAssured.baseURI = ROOT_URI;
        RestAssured.port = 8080;
    }

    public Response getRole(String... roleName) {
        String path = "";
        if(roleName != null && roleName.length != 0) {
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
