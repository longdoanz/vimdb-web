package com.viettel.imdb.rest.auto;

import com.viettel.imdb.rest.common.*;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.viettel.imdb.rest.common.Common.*;


public class DataTest extends TestHelper {

    @Test(priority = 3)
    public void test_Get_All_Record() throws Exception {
        String tableName = "TABLE_0";
        String key = "key-013";
        createTable(tableName);

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
        getRecords(tableName).andExpect(HttpStatus.OK).prettyPrint();
        dropRecord(tableName, key).andExpect(HttpStatus.NO_CONTENT);
    }

    //@Test(priority = 3)
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


    //@Test(priority = 6)
    public void testDelete2() {
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

    //@Test
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

    @Test(priority = 0) // api chua support
    public void get_Specific_Namespace() {
        getData("NAMESPACE").prettyPrint().andExpect(HttpStatus.OK);
    }

    @Test(priority = 0) // api chua support
    public void _2_4_1_getAllTable() throws Exception {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";
        String keyName2 = "key2";

        delete(tableName, keyName);
        delete(tableName, keyName2);
        createTable(tableName);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);
        createRecord(tableName, keyName2, bodyRecord).andExpect(HttpStatus.CREATED);

        HttpResponse res = getRecords(tableName).andExpect(HttpStatus.OK);
        List<String> keyList = res.read("[*].key");
        Assert.assertTrue(keyList.size() >= 2, "Must contain 2 keys response: " + keyList);
    }

    @Test(priority = 0)
    public void get_Specific_Namespace_With_Table() {
        getData("NAMESPACE?tables=tbl1").prettyPrint().andExpect(HttpStatus.OK);
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

    @Test(priority = 0)
    public void _2_4_7_createRecord() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);


        String ud_bodyRecord = "{\n" +
                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

//        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);
        updateRecord(tableName, keyName, ud_bodyRecord).andExpect(HttpStatus.NO_CONTENT);

        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(ud_bodyRecord);


    }

    @Test(priority = 0)
    public void _2_4_7_createRecord_existed() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);


        String bodyRecord2 = "{\n" +
                "  \"field1\": \"NAME33\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord2).andExpect(HttpStatus.BAD_REQUEST).andExpectResponse("error", "KEY_EXIST").andExpectResponse("message", "Error on");


    }

    @Test(priority = 0)
    public void _2_4_7_createRecord_existed_2() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);
        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.BAD_REQUEST).andExpectResponse("error", "KEY_EXIST").andExpectResponse("message", "Error on");


    }

    @Test(priority = 0)  /// ?? why P
    public void _2_4_7_createRecord_bitrungField() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field1\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);

    }

    @Test()
    public void _2_4_7_createRecord_field_hoa_thuong() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"Field1\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);

    }

    @Test()
    public void _2_4_7_createRecord_sai_syntax() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3),\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).prettyPrint().andExpect(HttpStatus.BAD_REQUEST).andExpectResponse("error", "INTERNAL_ERROR").andExpectResponse("message", "Error on");

        // get thong tin
//        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);



    }

    @Test()
    public void _2_4_7_createRecord_null() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "";

        createRecord(tableName, keyName, bodyRecord).prettyPrint().andExpect(HttpStatus.BAD_REQUEST).andExpectResponse("error", "BAD_REQUEST").andExpectResponse("message", "Error on");

    }

    @Test(priority = 0)
    public void _2_4_7_createRecord_space() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "              ";

        createRecord(tableName, keyName, bodyRecord).prettyPrint().andExpect(HttpStatus.BAD_REQUEST).andExpectResponse("error", "INTERNAL_ERROR").andExpectResponse("message", "Error on");;



    }

    @Test(priority = 0)
    public void _2_4_7_createRecord_validate_key() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "";

        for(int i= 0 ; i<256; i++)
            keyName = keyName + "a";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);

    }

    @Test(priority = 0)
    public void _2_4_7_createRecord_validate_key_() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "@%$%&^";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);

    }

    @Test(priority = 0)
    public void _2_4_7_createRecord_fieldnull() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3),\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).prettyPrint().andExpect(HttpStatus.BAD_REQUEST).andExpectResponse("error", "BAD_REQUEST").andExpectResponse("message", "Error on");;

        // get thong tin
//        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);

    }

    @Test(priority = 0)
    public void _2_4_7_createRecord_fieldspace() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"          \": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3),\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).prettyPrint().andExpect(HttpStatus.BAD_REQUEST).andExpectResponse("error", "INTERNAL_ERROR").andExpectResponse("message", "Error on");;

        // get thong tin
//        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);

    }
    @Test(priority = 0)
    public void _2_4_7_createRecord_sai_syntax_chuaspacetab() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord  //                "  \"key\": \""+keyName+"\",\n" +
                ;
        bodyRecord = "{\n" +
                "  \"field1\": \"NA       ME\",\n" +
                "  \"field2\": [1, \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).prettyPrint().andExpect(HttpStatus.CREATED);;

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);



    }




    @Test(priority = 0)
    public void _2_4_7_updateRecord() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);


        String ud_bodyRecord = "{\n" +
                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field5\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

//        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);
        updateRecord(tableName, keyName, ud_bodyRecord).andExpect(HttpStatus.NO_CONTENT);

        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(ud_bodyRecord);


    }
    @Test(priority = 0)
    public void _2_4_9_updateRecord_field_nhohon() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);

        String ud_bodyRecord = "{\n" +
                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
//                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

//        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);
        updateRecord(tableName, keyName, ud_bodyRecord).andExpect(HttpStatus.NO_CONTENT);

        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(ud_bodyRecord);


    }

    @Test(priority = 0)
    public void _2_4_9_updateRecord_field_khac1so() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);

        String ud_bodyRecord = "{\n" +
                "  \"key\": \""+keyName+"\",\n" +
                "  \"field0\": \"field1\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field4\": [1, 2, 3, 6, 7],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

//        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);
        updateRecord(tableName, keyName, ud_bodyRecord).andExpect(HttpStatus.NO_CONTENT);

        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(ud_bodyRecord);


    }

    @Test(priority = 0)
    public void _2_4_9_updateRecord_field_khachoantoan() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);

        String ud_bodyRecord = "{\n" +
                "  \"key\": \""+keyName+"\",\n" +
                "  \"field4\": \"NAME\",\n" +
                "  \"field5\": [1, 2, 3],\n" +
                "  \"field6\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

//        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);
        updateRecord(tableName, keyName, ud_bodyRecord).andExpect(HttpStatus.NO_CONTENT);

        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(ud_bodyRecord);


    }

    @Test(priority = 0)
    public void _2_4_9_updateRecord_field_lonhon() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);

        String ud_bodyRecord = "{\n" +
                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "  \"field4\": [1, 2, 3],\n" +
                "}";

//        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);
        updateRecord(tableName, keyName, ud_bodyRecord).andExpect(HttpStatus.NO_CONTENT);

        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(ud_bodyRecord);


    }

    @Test(priority = 0)
    public void _2_4_9_updateRecord_field_bang() {
        String tableName = "tbl1";
        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);

        String ud_bodyRecord = "{\n" +
                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
//                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

//        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);
        updateRecord(tableName, keyName, ud_bodyRecord).andExpect(HttpStatus.NO_CONTENT);

        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(ud_bodyRecord);


    }

    @Test(priority = 0)
    public void _2_4_9_updateRecord_not_exist() {
        String tableName = "table11";
        String keyName = "key1";
        String keyName2 = "key2";

        dropTable(tableName);
        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin

        String ud_bodyRecord = "{\n" +
//                "  \"key\": \""+keyName2+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        updateRecord(tableName, keyName2, ud_bodyRecord).prettyPrint().andExpect(HttpStatus.NOT_FOUND).andExpectResponse("error", "KEY_NOT_EXIST");

        // get thong tin


    }

    @Test(priority = 0)
    public void _2_4_10_deleteRecord() {
        String tableName = "table11";
        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(bodyRecord);


        String ud_bodyRecord = "{\n" +
                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        updateRecord(tableName, keyName, ud_bodyRecord).andExpect(HttpStatus.NO_CONTENT);
        // get thong tin
        findRecordPK(tableName, keyName).prettyPrint().andExpect(HttpStatus.OK).andExpectResponse(ud_bodyRecord);

        dropRecord(tableName, keyName).andExpect(HttpStatus.NO_CONTENT);


    }

    @Test(priority = 0)
    public void _2_4_10_deleteRecord_not_exist() {
        String tableName = "table11";
        String tableName2 = "table12";
        String keyName = "key1";
        String keyName2 = "key2";

        dropTable(tableName);
        dropTable(tableName2);

        createTable(tableName).andExpect(HttpStatus.CREATED);
        createTable(tableName2).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        // get thong tin

        String ud_bodyRecord = "{\n" +
                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        updateRecord(tableName, keyName, ud_bodyRecord).andExpect(HttpStatus.NO_CONTENT);
        // get thong tin

        dropRecord(tableName, keyName2).andExpect(HttpStatus.NOT_FOUND).andExpectResponse("error", "KEY_NOT_EXIST");

    }

    @Test(priority = 0)
    public void _2_4_11_runCmd() {
        String tableName = "table11";
        String keyName = "key1";

        dropTable(tableName);

        createTable(tableName).andExpect(HttpStatus.CREATED);

        String bodyRecord = "{\n" +
//                "  \"key\": \""+keyName+"\",\n" +
                "  \"field1\": \"NAME\",\n" +
                "  \"field2\": [1, 2, 3],\n" +
                "  \"field3\": {\n" +
                "    \"sub1\": \"SUB\"\n" +
                "  }\n" +
                "}";

        createRecord(tableName, keyName, bodyRecord).andExpect(HttpStatus.CREATED);

        String cmd = "create table 'table01';";
        runCmd(cmd).prettyPrint().andExpect(HttpStatus.OK);


    }




    // -------------------------------multiple test
    //@Test(priority = 0, invocationCount = 2) van bi loi TABLE IN USE
    public void _2_4_5_createTable_multi() {
        String tableName = "ty";
        dropTable(tableName);

//        createTable(tableName).andExpect(HttpStatus.CREATED);

        waitAllDone(new ArrayList<Runnable>() {{
            add(() -> {
                createTable(tableName).prettyPrint().andExpect(HttpStatus.CREATED);
            });
            add(() -> {
                createTable(tableName).prettyPrint().andExpect(HttpStatus.BAD_REQUEST).andExpectResponse("error", "TABLE_EXIST").andExpectResponse("message", "TABLE_EXIST");
            });
        }});
    }

    //@Test(priority = 0, invocationCount = 1) van bi loi TABLE IN USE
    public void _2_4_5_deleteTable_multi() throws  Exception{
        String tableName = "tydyuiyfd";
        dropTable(tableName).prettyPrint();

        createTable(tableName).andExpect(HttpStatus.CREATED);

        Thread.sleep(3000);


        waitAllDone(new ArrayList<Runnable>() {{
            add(() -> {
                dropTable(tableName).andExpect(HttpStatus.NO_CONTENT);
            });
            add(() -> {
                dropTable(tableName).prettyPrint().andExpect(HttpStatus.NOT_FOUND).andExpectResponse("error", "TABLE_NOT_EXIST").andExpectResponse("message", "TABLE_NOT_EXIST");
            });
        }});
    }


    @Test(priority = 0, invocationCount = 2) //van bi loi TABLE IN USE
    public void _2_4_5_deletemanyTable() throws  Exception{
        String tableName = "tydyuiyfd";
        String tableName2 = "tydyuiyfdr";
        dropTable(tableName).prettyPrint();
        dropTable(tableName2).prettyPrint();
        createTable(tableName).andExpect(HttpStatus.CREATED);

        Thread.sleep(3000);

        createTable(tableName2).andExpect(HttpStatus.CREATED);

        waitAllDone(new ArrayList<Runnable>() {{
            add(() -> {
                dropTable(tableName).prettyPrint().andExpect(HttpStatus.NO_CONTENT);
            });
            add(() -> {
                dropTable(tableName2).prettyPrint().andExpect(HttpStatus.NO_CONTENT);
            });
        }});
    }

    @Test
    public void test_Run_Command() {
        String tableName = "table01";
        runCmd(String.format("create table \"%s\"", tableName))
                .prettyPrint()
                .andExpect(HttpStatus.OK);
    }

    @Test
    public void test_Cmd_Select() {
//        String tableName =
        runCmd("create table \"customer\"").prettyPrint()
                .andExpect(HttpStatus.OK);

        String str = "aa";

        runCmd("insert into \"customer\" (key, json_value) values ('cus1', '{\n" +
                "  \"" + str + "\": {\n" +
                "  \"Id\": 1,\n" +
                "  \"Name\": \"Nguyen Van A\",\n" +
                "  \"Contact\": {\n" +
                "    \"Email\": \"anv12@vttel.com\",\n" +
                "    \"Tel\": {\n" +
                "      \"Viettel\": \"0983103127\",\n" +
                "      \"Vina\" : \"0943562589\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}')").prettyPrint()
                .andExpect(HttpStatus.OK);

        runCmd("select \"" + str + ".Contact\" from \"customer\" where key = 'cus1'").prettyPrint()
                .andExpect(HttpStatus.OK);
    }

}
