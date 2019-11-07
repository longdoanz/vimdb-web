package com.viettel.imdb.rest.auto;

import com.viettel.imdb.rest.common.TestHelper;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

public class UdfTest extends TestHelper {


    @Test(priority = 2)
    public void testSelectUdfNotFound() {
        String udfName = "UDF_01";
        getUdf(udfName).andExpect(HttpStatus.NOT_FOUND.value());
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

        createUdf(udfName, body).andExpect(HttpStatus.CREATED.value());
        getUdf(udfName).andExpect(HttpStatus.OK.value()).andExpectResponse("type", "LUA");

        String updateBody = "{\n" +
                "  \"content\": \"TOO LONG TO DISPLAY HERE\",\n" +
                "  \"name\": \"" + updateUdfName + "\",\n" +
                "  \"type\": \"LUA\"\n" +
                "}";
        updateUdf(udfName, updateBody).andExpect(HttpStatus.NO_CONTENT.value());

        getUdf(udfName).andExpect(HttpStatus.NOT_FOUND.value());

        getUdf(updateUdfName).andExpect(HttpStatus.OK.value()).andExpectResponse("type", "LUA");
        dropUdf(updateUdfName).andExpect(HttpStatus.NO_CONTENT.value());
    }

    @Test(priority = 2)
    public void test_Update_UDF() {
        String udfName = "UDF_01";
        String updateContent = "UPDATED_UDF";

        dropUdf(udfName);
        String body = "{\n" +
                "  \"content\": \"TOO LONG TO DISPLAY HERE\",\n" +
                "  \"type\": \"LUA\"\n" +
                "}";

        createUdf(udfName, body).andExpect(HttpStatus.CREATED.value());
        getUdf(udfName).andExpect(HttpStatus.OK.value()).andExpectResponse("type", "LUA");

        String updateBody = "{\n" +
                "  \"content\": \""+updateContent+"\",\n" +
                "  \"type\": \"LUA\"\n" +
                "}";
        updateUdf(udfName, updateBody).andExpect(HttpStatus.NO_CONTENT.value());

        getUdf(udfName).andExpect(HttpStatus.OK).andExpectResponse("content", updateContent);

        getUdf(udfName).andExpect(HttpStatus.OK.value()).andExpectResponse("type", "LUA");
        dropUdf(udfName).andExpect(HttpStatus.NO_CONTENT.value());
    }

    @Test(priority = 4)
    public void test_Update_UDF_Content() {
        String udfName = "UDF_01";
        String updateContent = "UPDATED_UDF";

        dropUdf(udfName);
        String body = "{\n" +
                "  \"content\": \"TOO LONG TO DISPLAY HERE\",\n" +
                "  \"type\": \"LUA\"\n" +
                "}";

        createUdf(udfName, body).andExpect(HttpStatus.CREATED.value());
        getUdf(udfName).andExpect(HttpStatus.OK.value()).andExpectResponse("type", "LUA");

        String updateBody = "{\n" +
                "  \"content\": \""+updateContent+"\"\n" +
                "}";
        updateUdf(udfName, updateBody).andExpect(HttpStatus.NO_CONTENT.value());

        getUdf(udfName).andExpect(HttpStatus.OK).andExpectResponse("content", updateContent);

        getUdf(udfName).andExpect(HttpStatus.OK.value()).andExpectResponse("type", "LUA");
        dropUdf(udfName).andExpect(HttpStatus.NO_CONTENT.value());
    }
}
