package com.viettel.imdb.rest;

import com.viettel.imdb.rest.common.HTTPRequest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.viettel.imdb.rest.common.Common.*;
import static org.testng.Assert.assertEquals;
/**
 * @author longdt20
 * @since 16:23 15/02/2019
 */

public class TableAPITest {

    private HTTPRequest http;

    @BeforeMethod
    public void setUp() throws Exception {
        http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);
    }

    @Test
    public void testCreateTable() throws Exception {
        String tableName = "";
        for(int i = 0; i < 255; i++) {
            tableName += "a";
        }
        String body = "{ \"name\": \""+tableName+"\" }";
        Map res = http.sendPost(DB_PATH, body);
        System.out.println(res);
        http.sendDelete(buildFromPath(DB_PATH, tableName));
        assertEquals(res.get("code"), HttpStatus.CREATED.value());
    }

    @Test
    public void testCreateTable_2() throws Exception {

        String body = "{ \"name\": \"table01\" }";
        Map res = http.sendPost(DB_PATH, body);
        System.out.println(res);
        assertEquals(res.get("code"), HttpStatus.CREATED.value());
    }

    @Test
    public void testCreateTable_3() throws Exception {

        Map<String, String> header = new HashMap<String, String> () {{
            put("Content-Type", "application/x-www-form-urlencoded");
            put("Accept", "application/x-www-form-urlencoded");
            put("Cookie", http.getCookie());
        }};
        String body = "{ \"name\": \"table01\" }";
        Map res = http.sendWithData("POST", DB_PATH, header, body);
        System.out.println(res);
        assertEquals(res.get("code"), HttpStatus.NOT_ACCEPTABLE.value());
    }

    @Test
    public void testDeleteTable() throws Exception {

        String tableName = "table01";
        Map res = http.sendDelete(buildFromPath(DB_PATH, tableName));
        System.out.println(res);
        assertEquals(res.get("code"), HttpStatus.NO_CONTENT.value());
    }

}
