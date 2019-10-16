package com.viettel.imdb.rest;

import com.viettel.imdb.rest.common.HTTPRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static com.viettel.imdb.rest.common.Common.*;

public class UDFTest {
    private HTTPRequest http;
    @BeforeMethod
    public void setUp() throws Exception {
        //http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);
        http = new HTTPRequest(HOST_URL);
    }
    @Test
    public void httpCookie() {
        System.out.println(http.getCookie());
    }

    @Test
    public void getUDFsList() throws Exception {
        http.sendGet(buildFromPath("/v1/udf/"));
    }
    @Test
    public void getinsertUDF() throws Exception {
        for(int i = 0; i < 10; i++){
            String body = "{\n" +
                    "  \"udf_name\": \"thurv"+i+"\",\n" +
                    "  \"type\": null,\n" +
                    "  \"syncedOnAllNodes\": true,\n" +
                    "  \"content\": \"abzxyz\"\n" +
                    "}";
            Map res = http.sendPost(buildFromPath("/v1/udf/thurv"+i), body);
            System.out.println(res);
        }

    }
    @Test
    public void editUDF() throws Exception {
        String body = "{\n" +
                "  \"udf_name\": \"thurv4\",\n" +
                "  \"type\": null,\n" +
                "  \"syncedOnAllNodes\": true,\n" +
                "  \"content\": \"ABCXYZ\"\n" +
                "}";
        Map res = http.sendPut(buildFromPath("/v1/udf/thurv4"), body);
        System.out.println(res);
    }
    @Test
    public void delete() throws Exception {
        http.sendDelete(buildFromPath("/v1/udf/thurv1"));
    }

}
