package com.viettel.imdb.rest.restassured;

import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

public class RoleTest extends TestHelper {

    @Test(priority = 2)
    public void getAllRole() {
        Response res = getRole();
        res.prettyPrint();
        res.then().statusCode(HttpStatus.OK.value());
    }

    @Test(priority = 2)
    public void testCreateDropRole() {
        String roleName = "ROLE_01";
        String body = "{\n" +
                "  \"privileges\": [\n" +
                "{\n" +
                "        \"permission\": \"read\",\n" +
                "        \"resource\": {\n" +
                "          \"name\": \"data\",\n" +
                "          \"namespace\": \"NAMESPACE\",\n" +
                "          \"table\": \"SessionData\"\n" +
                "        }\n" +
                "      }" +
                "],\n" +
                "  \"name\": \"" + roleName + "\"\n" +
                "}";

        Response res = createRole(body);
        res.prettyPrint();
        res.then().statusCode(201);
        dropRole(roleName);
    }

    @Test(priority = 2)
    public void testDropAndGetRole() {
        String roleName = "ROLE_02";
        dropRole(roleName);
        String body = "{\n" +
                "  \"name\": \""+ roleName +"\"\n" +
                "}";

        createRole(body).then().statusCode(HttpStatus.CREATED.value());

        getRole(roleName).then().statusCode(HttpStatus.OK.value()).body("privileges", Matchers.hasSize(0));

        dropRole(roleName).then().statusCode(HttpStatus.NO_CONTENT.value());
    }


    @Test(priority = 3)
    public void testUpdateRole() {
        String roleName = "ROLE_03";
        dropRole(roleName);
        String body = "{\n" +
                "  \"name\": \""+ roleName +"\"\n" +
                "}";

        createRole(body).then().statusCode(HttpStatus.CREATED.value());

        getRole(roleName).then().statusCode(HttpStatus.OK.value()).body("privileges", Matchers.hasSize(0));

        dropRole(roleName).then().statusCode(HttpStatus.NO_CONTENT.value());
    }


    @Test(priority = 4)
    public void testPatchRole() {
        String roleName = "ROLE_04";
        String body = "{\n" +
                "  \"privileges\": [\n" +
                "{\n" +
                "        \"permission\": \"read\",\n" +
                "        \"resource\": {\n" +
                "          \"name\": \"data\",\n" +
                "          \"namespace\": \"NAMESPACE\",\n" +
                "          \"table\": \"SessionData\"\n" +
                "        }\n" +
                "      }" +
                "],\n" +
                "  \"name\": \"" + roleName + "\"\n" +
                "}";

        createRole(body);

        String updateRoleBody = "{\n" +
                "  \"name\": \""+ roleName +"\"\n" +
                "}";

        updateRole(roleName, updateRoleBody).then().statusCode(HttpStatus.NO_CONTENT.value());

        getRole(roleName).then().statusCode(HttpStatus.OK.value()).body("privileges", Matchers.hasSize(0));

        dropRole(roleName).then().statusCode(HttpStatus.NO_CONTENT.value());
    }


}
