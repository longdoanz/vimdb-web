package com.viettel.imdb.rest;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.viettel.imdb.rest.common.HTTPRequest;
import com.viettel.imdb.util.IMDBEncodeDecoder;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.JSONParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static com.viettel.imdb.rest.common.Common.HOST_URL;
import static com.viettel.imdb.rest.common.Common.buildFromPath;

public class BackupRestoreTest {
    private HTTPRequest http;
    private IMDBEncodeDecoder encoder;
    @BeforeMethod
    public void setUp() throws Exception {
        //http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);
        http = new HTTPRequest(HOST_URL);
        encoder = IMDBEncodeDecoder.getInstance();
    }

    @Test
    public void backupTest() throws Exception {
        /*String body = "{\n" +
                "  \"clusterAuthInfo\": {" +
                "  \"needAuthen\": true," +
                "  \"username\": \"admin\"," +
                "  \"password\": \"admin\"" +
                "},\n" +
                "  \"clusterNodeSSHInfo\": {" +
                "  \"ip\": \"142.3.45.5.6\"," +
                "  \"username\": \"admin\"," +
                "  \"password\": \"admin\"" +
                "  },\n" +
                "  \"backupConfig\": {" +
                "  \"isPartionRange\": true," +
                "  \"partionRangeStart\": 0," +
                "  \"backupPartionEnd\": 1234," +
                "  \"partionList\": [" +
                "    1,2,3,4,5" +
                "  ]\n" +
                "  \"backupDirectory\": \"backupDirectory\"" +
                "   }\n" +
                "}";*/
//        String body = "{\n" +
//                "  \"clusterAuthInfo\": null,\n" +
//                "  \"clusterNodeSSHInfo\": null,\n" +
//                "  \"backupConfig\": null\n" +
//                "}";
        String body = "{\"clusterAuthInfo\":{\"needAuthen\":false,\"username\":\"admin\",\"password\":\"admin\"},\"clusterNodeSSHInfo\":{\"ip\":null,\"username\":null,\"password\":null},\"backupConfig\":{\"partionRangeStart\":0,\"backupPartionEnd\":4096,\"partionList\":[1,2,3,5,10],\"backupDirectory\":\"backupDirectory\",\"partionRange\":true}}";
        Map res = http.sendPost(buildFromPath("/v1/tool/backup"),body);
//        JSONValue jsonValue = new JSONValue();
//        JSONObject object = (JSONObject) jsonValue.parse(res.get("response").toString());
        System.out.println(res);
        JsonNode actualResult = encoder.decodeJsonNode(encoder.encodeJsonString(res.get("response").toString())).get("callback");
        System.out.println(actualResult);
        String callback =  actualResult.toString();
        callback = callback.substring(1, callback.length()-1);
        //get process status
        Map res2;
        String status;
        do{
            res2 = http.sendGet(buildFromPath(callback));
            status = res2.get("response").toString();
            System.out.println(res2);
        }while(status.equals("{\"status\":\"processing\"}"));
    }

    @Test
    public void backupProcessStatusTest() throws Exception {
//        Map res = http.sendGet(buildFromPath("/v1/tool/backup"), "process=7970404196631695392");
//        JSONParser parser = new JSONParser();
//
//        JSONObject obj = (JSONObject)new JSONParser().parse(res.get("response").toString());
//
//        System.out.println(res.get("response"));
    }
    @Test
    public void restoreTest() throws Exception {
//        String body = "{\n" +
//                "  \"clusterAuthInfo\": null,\n" +
//                "  \"clusterNodeSSHInfo\": null,\n" +
//                "  \"restoreConfig\": null\n" +
//                "}";
        String body = "{\"clusterAuthInfo\":{\"needAuthen\":false,\"username\":\"admin\",\"password\":\"admin\"},\"clusterNodeSSHInfo\":{\"ip\":null,\"username\":null,\"password\":null},\"restoreConfig\":{\"partionRange\":true, \"partionRangeStart\":0,\"backupPartionEnd\":4096,\"partionList\":[1,2,3,5,10],\"restoreDirectory\":\"restoreDirectory\"}}";
        Map res = http.sendPost(buildFromPath("/v1/tool/restore"),body);

        JsonNode actualResult = encoder.decodeJsonNode(encoder.encodeJsonString(res.get("response").toString())).get("callback");
        System.out.println(actualResult);
        String callback =  actualResult.toString();
        callback = callback.substring(1, callback.length()-1);
        //get process status
        Map res2;
        String status;
        do{
            res2 = http.sendGet(buildFromPath(callback));
            status = res2.get("response").toString();
            System.out.println(res2);
        }while(status.equals("{\"status\":\"processing\"}"));
    }
    @Test
    public void restoreProcessStatusTest() throws Exception {
        //http.sendGet(buildFromPath("/v1/tool/restore/5590567581940002998"));
    }
}
