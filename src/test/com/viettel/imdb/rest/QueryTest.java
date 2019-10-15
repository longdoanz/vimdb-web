package com.viettel.imdb.rest;

import com.viettel.imdb.rest.common.HTTPRequest;
import com.viettel.imdb.rest.common.QueryParam;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static com.viettel.imdb.rest.common.Common.*;
import static org.testng.Assert.assertEquals;

/**
 * @author longdt20
 * @since 10:00 26/02/2019
 */

public class QueryTest {
    private HTTPRequest http;

    @BeforeMethod
    public void setUp() throws Exception {
        http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);
    }

    @Test
    void test_01() {

        /*//        Create index
        String bodyRequest = "{\"name\":  \"sub1\"}";
        Map res = http.sendPost(buildFromPath(DB_PATH, tableName, INDEX_PATH), bodyRequest);
        System.out.println(res);

        QueryParam queryParam = new QueryParam("{\"sub1\":145}");

        String expectData = "{\"results\":[{\"key\":\"key-03\",\"record\":{\"sub1\":145,\"sub2\":[1,3,5],\"sub3\":{\"ssub1\":[1],\"SUB23\":{\"S42\":\"TESTING\"}},\"sub4\":\"String\"}}]}";
        res = http.sendGet(buildFromPath(DB_PATH, tableName), queryParam.getQuery());
        System.out.println(res);
        assertEquals(res.get("code"), HttpStatus.OK.value());
        assertEquals(res.get("response").toString(), expectData);*/
    }
}
