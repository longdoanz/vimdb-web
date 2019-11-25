package com.viettel.imdb.rest.auto;

import com.viettel.imdb.rest.common.*;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import static com.viettel.imdb.rest.common.Common.*;


public class DataTest extends TestHelper {

    @Test(priority = 3)
    public void testInsertDeleteRecord() throws Exception {
        String tableName = "TABLE_0";
        String key = "key-013";
//        http.sendDelete(buildFromPath(DB_PATH, tableName));
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String body = "{\n" +
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
//        HttpResponse res = http.sendPost(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, "key-03"), body, token);
        insert(tableName, key, body).andExpect(HttpStatus.CREATED);

        select(tableName, key).andExpect(HttpStatus.OK)
                .andExpectResponse(body);

        String updateBody = "{\n" +
                "  \"sub1\": 145,\n" +
                "  \"sub3\": {\n" +
                "    \"ssub1\": [1]\n" +
                "  }\n" +
                "}";

        update(tableName, key, updateBody).andExpect(HttpStatus.NO_CONTENT);

        select(tableName, key).andExpect(HttpStatus.OK)
                .andExpectResponse(updateBody);

        delete(tableName, key).andExpect(HttpStatus.NO_CONTENT);
    }


    @Test(priority = 6)
    public void testDelete2() throws Exception {
        String tableName = "TABLE_0";
        String keyName = "key1";
        String bodyRecord = "{\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

//        getRecord(tableName, keyName).andExpect(HttpStatus.OK).prettyPrint();
//        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);
    }

    @Test
    public void testCreateTable_() throws Exception {
        String tableName = "TABLE_NAME02re";
        dropTable(tableName);
        testCreateTable(tableName, HttpStatus.CREATED, null);
        HttpResponse res = http.sendGet(DATA_PATH);
        System.out.println(res);
    }


    //THANHNT113 // create record dang bi Fail -> chua test cac api duoi

    @Test(priority = 0) // api chua support
    public void _2_4_1_getAllNamespace() {
        getData().prettyPrint().andExpect(HttpStatus.OK);
    }

    @Test(priority = 0)
    public void _2_4_5_createTable() {
        String tableName = "table1224tred";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);
    }

    @Test(priority = 0)
    public void _2_4_5_createTable_existed() {
        String tableName = "table1224tred";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);
        createTable(tableName).andExpect(HttpStatus.BAD_REQUEST);

    }

    @Test(priority = 0)
    public void _2_4_6_deleteTable() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        dropTable(tableName).andExpect(HttpStatus.NO_CONTENT);
    }

    @Test(priority = 0)
    public void _2_4_6_deleteTable_not_existed() throws Exception {
        String tableName = "tbl1";
        dropTable(tableName);

        dropTable(tableName).andExpect(HttpStatus.NOT_FOUND);
    }

    @Test(priority = 0) // F
    public void _2_4_7_createRecord() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";
        String bodyRecord = "{\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

    }

    @Test(priority = 0) // F
    public void _2_4_7_createRecord_existed() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";
        String bodyRecord = "{\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);
        getRecord(tableName, keyName).andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);
        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.BAD_REQUEST);
    }

    @Test(priority = 0)
    public void _2_4_9_updateRecord() {
    }

}
