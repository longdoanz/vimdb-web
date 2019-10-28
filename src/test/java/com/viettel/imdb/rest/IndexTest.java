package com.viettel.imdb.rest;

import com.viettel.imdb.rest.common.HTTPRequest;
import com.viettel.imdb.rest.common.HttpResponse;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static com.viettel.imdb.rest.common.Common.*;
import static org.testng.Assert.assertEquals;

/**
 * @author longdt20
 * @since 17:45 20/02/2019
 */

public class IndexTest {
    private HTTPRequest http;

    @BeforeMethod
    public void setUp() throws Exception {
        http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);
    }

    @Test
    public void testCreateIndex() throws Exception {
        String tableName = "table01";
        String fieldName = "sub";

//        Create table
        String body = "{ \"name\": \""+tableName+"\" }";
        http.sendPost(DB_PATH, body);

//        Create index
        String bodyRequest = "{\"name\":  \"" + fieldName + "\"}";
        HttpResponse res = http.sendPost(buildFromPath(DB_PATH, tableName, INDEX_PATH), bodyRequest);
        System.out.println(res);
        assertEquals(res.getStatus(), HttpStatus.CREATED);
    }


    @Test
    public void testDropIndex() throws Exception {

        String tableName = "table01";
        String fieldName = "sub";
        HttpResponse res = http.sendDelete(buildFromPath(DB_PATH, tableName, INDEX_PATH, fieldName));
        System.out.println(res);
        assertEquals(res.getStatus(), HttpStatus.NO_CONTENT);
    }
}
