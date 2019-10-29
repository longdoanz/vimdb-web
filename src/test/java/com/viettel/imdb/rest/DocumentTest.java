package com.viettel.imdb.rest;

import com.viettel.imdb.rest.common.HTTPRequest;
import com.viettel.imdb.rest.common.HttpResponse;
import com.viettel.imdb.rest.common.QueryParam;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import static com.viettel.imdb.rest.common.Common.*;
import static org.testng.Assert.assertEquals;

/**
 * @author longdt20
 * @since 16:46 14/02/2019
 */

public class DocumentTest {

    private HTTPRequest http;


    String tableName = "TABLE_NAME";
    String recordKey = "key-03";

    /*@BeforeMethod
    public void setUp() throws Exception {
        http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);
//        http.sendDelete(buildFromPath(DB_PATH, tableName));
        String body = "{ \"name\": \"table01\" }";
        http.sendPost(DB_PATH, body);
    }*/

    @Test
    public void httpCookie() {
        System.out.println(http.getToken());
    }

    @Test(priority = 1)
    public void testInsert() throws Exception {
        http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);
//        http.sendDelete(buildFromPath(DB_PATH, tableName));
        String body = "{ \"tableName\": \""+ tableName + "\" }";
        http.sendPost(DATA_PATH_WITH_NAMESPACE, body);

        String body2 = "{\n" +
                "  \"sub1\": 145,\n" +
                "  \"sub4\": \"String\",\n" +
                "  \"sub2\": [1, 3, 5],\n" +
                "  \"sub3\": {\n" +
                "    \"ssub1\": [1],\n" +
                "    \"SUB23\": {\n" +
                "      \"S42\": \"TESTING\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        HttpResponse res = http.sendPost(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, "key-03"), body2);
        System.out.println(res);
        assertEquals(res.getStatus(), HttpStatus.CREATED);

    }



    @Test(priority = 2)
    public void testSelectKey() throws Exception {
        http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);

        String expectData = "{\"sub1\":145,\"sub2\":[1,3,5],\"sub3\":{\"ssub1\":[1],\"SUB23\":{\"S42\":\"TESTING\"}},\"sub4\":\"String\"}";
        HttpResponse res = http.sendGet(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, recordKey));
        System.out.println(res);
        assertEquals(res.getStatus(), HttpStatus.OK);
        assertEquals(res.getResponse().toString(), expectData);
    }


    @Test(priority = 2)
    public void testDeleteKey() throws Exception {
        http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);

        HttpResponse res = http.sendDelete(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, recordKey));
        System.out.println(res);
        assertEquals(res.getStatus(), HttpStatus.NO_CONTENT);
    }



    @Test(priority = 2, description = "Using scan API")
    public void testSelectWithSecondaryIndexField_2() throws Exception {

        //        Create index
        String bodyRequest = "{\"name\":  \"sub1\"}";
        HttpResponse res = http.sendPost(buildFromPath(DB_PATH, tableName, INDEX_PATH), bodyRequest);
        System.out.println(res);

        QueryParam queryParam = new QueryParam("{\"sub1\":{\"$bt\": [10,150]}}");

        String expectData = "{\"results\":[{\"key\":\"key-03\",\"record\":{\"sub1\":145,\"sub2\":[1,3,5],\"sub3\":{\"ssub1\":[1],\"SUB23\":{\"S42\":\"TESTING\"}},\"sub4\":\"String\"}}]}";
        res = http.sendGet(buildFromPath(DB_PATH, tableName), queryParam.getQuery());
        System.out.println(res);
        assertEquals(res.getStatus(), HttpStatus.OK);
        assertEquals(res.getResponse().toString(), expectData);
    }

    @Test(priority = 3, description = "Using scan API get list Field Name")
    public void testSelectSecondaryIndexField_3() throws Exception {

        //        Create index
        String bodyRequest = "{\"name\":  \"sub1\"}";
        HttpResponse res = http.sendPost(buildFromPath(DB_PATH, tableName, INDEX_PATH), bodyRequest);
        System.out.println(res);

//        String filter = "{\"sub1\":145}&fields=$.sub1,$.sub2,$.sub3";
        QueryParam queryParam = new QueryParam("{\"sub1\":145}", "$.sub1,$.sub2");

        String expectData = "{\"results\":[{\"key\":\"key-03\",\"record\":{\"sub1\":145,\"sub2\":[1,3,5]}}]}";
        res = http.sendGet(buildFromPath(DB_PATH, tableName), queryParam.getQuery());
        System.out.println(res);

        res.andExpect(HttpStatus.OK)
                .andExpectResponse(expectData);
    }


    @Test(priority = 3)
    public void testUpdate() throws Exception {
        http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);

        String body = "{\n" +
                "  \"sub1\": \"TESTING 1\",\n" +
                "  \"sub2\": \"Testing 1111\"\n" +
                "}";
        String expectData = "{\"sub1\":\"TESTING 1\",\"sub2\":\"Testing 1111\",\"sub3\":{\"ssub1\":[1],\"SUB23\":{\"S42\":\"TESTING\"}},\"sub4\":\"String\"}";
        HttpResponse res = http.sendPut(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, recordKey), body);
        System.out.println(res);
        assertEquals(res.getStatus(), HttpStatus.NO_CONTENT);
        res = http.sendGet(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, recordKey));
        System.out.println(res);

        res.andExpect(HttpStatus.OK)
                .andExpectResponse(expectData);
    }

    @Test(priority = 4)
    public void testUpdate_3() throws Exception {
        String body = "{\"a\": \"b\"}";
        String expectData = "{\"a\":\"b\",\"sub1\":\"TESTING 1\",\"sub2\":\"Testing 1111\",\"sub3\":{\"ssub1\":[1],\"SUB23\":{\"S42\":\"TESTING\"}},\"sub4\":\"String\"}";
        HttpResponse res = http.sendPut(buildFromPath(DB_PATH, tableName, recordKey), body);
        System.out.println(res);
        res.andExpect(HttpStatus.NO_CONTENT);

        res = http.sendGet(buildFromPath(DB_PATH, tableName, recordKey));
        System.out.println(res);
        res.andExpect(HttpStatus.OK)
                .andExpectResponse(expectData);
    }

    @Test(priority = 5)
    public void testDelete() throws Exception {
        HttpResponse res = http.sendDelete(buildFromPath(DB_PATH, tableName, recordKey));
        System.out.println(res);
        res.andExpect( HttpStatus.NO_CONTENT);
    }
}