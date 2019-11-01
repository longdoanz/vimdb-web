package com.viettel.imdb.rest.restassured;

import org.hamcrest.Matchers;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

public class UdfTest extends TestHelper {


    @Test(priority = 2)
    public void testSelectUdfNotFound() {
        String udfName = "UDF_01";
        getUdf(udfName).then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test(priority = 2)
    public void testGetUdfList() {
        String udfName = "UDF_01";
        String updateUdfName = "UPDATED_UDF";

        dropUdf(udfName);
        String body = "{\n" +
                "  \"content\": \"TOO LONG TO DISPLAY HERE\",\n" +
                "  \"type\": \"LUA\"\n" +
                "}";

        createUdf(udfName, body).then().statusCode(HttpStatus.CREATED.value());
        getUdf(udfName).then().statusCode(HttpStatus.OK.value()).body("type", Matchers.is("LUA"));

        String updateBody = "{\n" +
                "  \"content\": \"TOO LONG TO DISPLAY HERE\",\n" +
                "  \"name\": \"" + updateUdfName + "\",\n" +
                "  \"type\": \"LUA\"\n" +
                "}";
        updateUdf(udfName, updateBody).then().statusCode(HttpStatus.NO_CONTENT.value());

        getUdf(udfName).then().statusCode(HttpStatus.NOT_FOUND.value());

        getUdf(updateUdfName).then().statusCode(HttpStatus.OK.value()).body("type", Matchers.is("LUA"));
        dropUdf(updateUdfName).then().statusCode(HttpStatus.NO_CONTENT.value());
    }
}
