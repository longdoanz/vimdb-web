package com.viettel.imdb.rest.service;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.rest.common.HTTPRequest;
import com.viettel.imdb.rest.common.TestUtil;
import com.viettel.imdb.util.IMDBEncodeDecoder;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static com.viettel.imdb.rest.common.Common.*;
import static org.testng.Assert.assertEquals;

/**
 * @author quannh22
 * @since 10/08/2019
 */
public class DataServiceImplTest extends TestUtil {
    private HTTPRequest http;
    private IMDBEncodeDecoder encoder;
    String token;
    @BeforeMethod
    public void setUp() throws Exception {
        http = new HTTPRequest(HOST_URL);
        encoder = IMDBEncodeDecoder.getInstance();
        token = createAuthenticationToken("admin", "admin", HttpStatus.OK, null);
        System.out.println("Token: " + token);
    }
    @Test(priority = 1)
    public void testGetDataInfo() throws Exception {
        final String TABLE_PREFIX = "TABLE_";
        http.sendGetWithToken("/v1/data/", token);
    }

    @Test(priority = 1)
    public void testCreateTableValid() throws Exception {
        final String TABLE_PREFIX = "TABLE_";
        for(int i = 0; i < 100; i++) {
            testCreateTable(TABLE_PREFIX + i, HttpStatus.CREATED, null);
            testDropTable(TABLE_PREFIX + i, HttpStatus.NO_CONTENT, null);
        }
    }

    @Test(priority = 2)
    public void testCreateTableExisted() throws Exception {
        final String TABLE_PREFIX = "TABLE_";
        for(int i = 0; i < 100; i++) {
            testCreateTable(TABLE_PREFIX + i, HttpStatus.CREATED, null);
        }
        for(int i = 0; i < 100; i++) {
            String expectedResponse = "{\"error\":\"" + ErrorCode.TABLE_EXIST.name() + "\"}";
            testCreateTable(TABLE_PREFIX + i, HttpStatus.BAD_REQUEST, expectedResponse);
        }
    }

    @Test(priority = 3)
    public void testCreateTableWithInvalidTableName() {
        String tableName = "InvalidU*()U()J:LFKDSJFKL";
        String expectedResponse = "{\"error\":\"" + ErrorCode.TABLENAME_INVALID.name() + "\"}";
        testCreateTable(tableName, HttpStatus.BAD_REQUEST, expectedResponse);
    }

    @Test(priority = 4)
    public void testDropTable() {
        final String TABLE_PREFIX = "TABLE_";
        for(int i = 0; i < 100; i++) {
            testCreateTable(TABLE_PREFIX + i, HttpStatus.CREATED, null);
        }
        for(int i = 0; i < 100; i++) {
            testDropTable(buildFromPath(DATA_PATH_WITH_NAMESPACE, TABLE_PREFIX + i), HttpStatus.NO_CONTENT, null);
        }
    }

    @Test(priority = 5)
    public void testCreateIndex() {
        final String TABLE_NAME = "TABLE_0";
        final String INDEX_NAME = "$.justAnIndexName";
        testCreateTable(TABLE_NAME, HttpStatus.CREATED, null);
        testCreateIndex(TABLE_NAME, INDEX_NAME, HttpStatus.CREATED, null);
        testDropTable(TABLE_NAME, HttpStatus.NO_CONTENT, null);
    }

    @Test(priority = 6)
    public void testDropIndex() {
    }

    @Test(priority = 7)
    public void testSelect() throws Exception {
        StringBuilder tableName = new StringBuilder();
        for(int i = 0; i < 255; i++) {
            tableName.append("a");
        }
        Map res = http.sendGet(DATA_PATH + "/namespace" + "/TABLE_1" + "/key", "fieldnames=field1&fieldnames=field2&fieldnames=field3&fieldnames=field4");
        System.out.println(res);
    }


    @Test(priority = 3)
    public void testInsert1() throws Exception {
        String TABLE_NAME = "TABLE_0";
//        http.sendDelete(buildFromPath(DB_PATH, tableName));
        String body = "{ \"name\": \""+ TABLE_NAME + "\" }";
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
        Map res = http.sendPost(buildFromPath(DATA_PATH_WITH_NAMESPACE, TABLE_NAME, "key-03"), body2);
        System.out.println(res);
        assertEquals(res.get("code"), HttpStatus.CREATED.value());

    }

    @Test(priority = 4)
    public void testSelect2() throws Exception {
        String TABLE_NAME = "TABLE_0";
        String key = "key-03";

        String expectData = "{\"sub1\":145,\"sub2\":[1,3,5],\"sub3\":{\"ssub1\":[1],\"SUB23\":{\"S42\":\"TESTING\"}},\"sub4\":\"String\"}";
        Map res = http.sendGet(buildFromPath(DATA_PATH_WITH_NAMESPACE, TABLE_NAME, key));
        System.out.println(res);
        assertEquals(res.get("code"), HttpStatus.OK.value());
        assertEquals(res.get("response").toString(), expectData);
    }

    @Test(priority = 5)
    public void testUpdate() throws Exception {
        String TABLE_NAME = "TABLE_0";
        String key = "key-03";

        String body = "{\n" +
                "  \"sub1\": \"TESTING 1\",\n" +
                "  \"sub2\": \"Testing 1111\"\n" +
                "}";
        String expectData = "{\"sub1\":\"TESTING 1\",\"sub2\":\"Testing 1111\",\"sub3\":{\"ssub1\":[1],\"SUB23\":{\"S42\":\"TESTING\"}},\"sub4\":\"String\"}";
        Map res = http.sendPut(buildFromPath(DATA_PATH_WITH_NAMESPACE, TABLE_NAME, key), body);
        System.out.println(res);
        assertEquals(res.get("code"), HttpStatus.NO_CONTENT.value());
        res = http.sendGet(buildFromPath(DATA_PATH_WITH_NAMESPACE, TABLE_NAME, key));
        System.out.println(res);
        assertEquals(res.get("code"), HttpStatus.OK.value());
        assertEquals(res.get("response").toString(), expectData);
    }

    @Test(priority = 6)
    public void testDelete2() throws Exception {
        String TABLE_NAME = "TABLE_0";
        String key = "key-03";

        Map res = http.sendDelete(buildFromPath(DATA_PATH_WITH_NAMESPACE, TABLE_NAME, key));
        System.out.println(res);
        assertEquals(res.get("code"), HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void testUpsert() {
    }

    @Test
    public void testUpsert1() {
    }

    @Test
    public void testReplace() {
    }

    @Test
    public void testReplace1() {
    }

    @Test
    public void testScan() {
    }

    @Test
    public void testDelete() {
    }

    @Test
    public void testCreateTable() throws Exception {
        testCreateTable("TABLE_NAME02", HttpStatus.CREATED, null);
        Map res = http.sendGet(DATA_PATH);
        System.out.println(res);
    }
}