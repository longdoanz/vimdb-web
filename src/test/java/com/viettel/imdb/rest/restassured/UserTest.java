package com.viettel.imdb.rest.restassured;

import org.hamcrest.Matchers;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

public class UserTest extends TestHelper {

    @Test(priority = 2)
    public void testCreateDropUser() {
        String userName = "USER_01";

        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \"admin13\",\n" +
                "  \"roles\": [\n" +
                "    \"read-write.data.CustInfo\",\n" +
                "    \"read-write.data.MappingSubCust\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + userName + "\"\n" +
                "}";
        createUser(createUserBody).then().statusCode(HttpStatus.CREATED.value());
        getUser(userName).then().statusCode(HttpStatus.OK.value()).body("roles", Matchers.hasSize(2));
        dropUser(userName);
        getUser(userName).then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test(priority = 3)
    public void testCreate_Get_Update_Drop() {
        String userName = "USER_03";
        dropUser(userName);
        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \"admin13\",\n" +
                "  \"roles\": [\n" +
                "    \"read-write.data.CustInfo\",\n" +
                "    \"read-write.data.MappingSubCust\",\n" +
                "    \"read-write.user.SessionData\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + userName + "\"\n" +
                "}";
        createUser(createUserBody).then().statusCode(HttpStatus.CREATED.value());
        getUser(userName).then().statusCode(HttpStatus.OK.value()).body("roles", Matchers.hasSize(3));

        String updateBody = "{\n" +
                "\n" +
                "  \"password\": \"admin13\",\n" +
                "  \"roles\": [" +
                "]\n" +
                "}";
        updateUser(userName, updateBody);
        getUser(userName).then().statusCode(HttpStatus.OK.value()).body("roles", Matchers.hasSize(0));
        dropUser(userName);
        getUser(userName).then().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
