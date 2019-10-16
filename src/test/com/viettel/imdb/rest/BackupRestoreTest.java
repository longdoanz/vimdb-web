package com.viettel.imdb.rest;

import com.viettel.imdb.rest.common.HTTPRequest;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static com.viettel.imdb.rest.common.Common.HOST_URL;
import static com.viettel.imdb.rest.common.Common.buildFromPath;

public class BackupRestoreTest {
    private HTTPRequest http;
    @BeforeMethod
    public void setUp() throws Exception {
        //http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);
        http = new HTTPRequest(HOST_URL);
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
        String body = "{\"clusterAuthInfo\":{\"needAuthen\":false,\"username\":null,\"password\":null},\"clusterNodeSSHInfo\":{\"ip\":null,\"username\":null,\"password\":null},\"backupConfig\":{\"partionRangeStart\":0,\"backupPartionEnd\":4096,\"partionList\":[1,2,3,5,10],\"backupDirectory\":\"backupDirectory\",\"partionRange\":true}}";
        Map res = http.sendPost(buildFromPath("/v1/tool/backup"),body);


        String processId = String.valueOf(res.get("response"));
        System.out.println(processId);
        //get process status
        Map res2;
        String status;
        do{
            res2 = http.sendGet(buildFromPath("/v1/tool/backup"), "process="+processId);
            status = res2.get("response").toString();
            System.out.println(res2);
        }while(status.equals("{\"status\":\"processing\"}"));
    }

    @Test
    public void backupProcessStatusTest() throws Exception {
        Map res = http.sendGet(buildFromPath("/v1/tool/backup"), "process=7970404196631695392");
        JSONParser parser = new JSONParser();

        JSONObject obj = (JSONObject)new JSONParser().parse(res.get("response").toString());

        System.out.println(res.get("response"));
    }
    @Test
    public void restoreTest() throws Exception {
        String body = "{\n" +
                "  \"clusterAuthInfo\": null,\n" +
                "  \"clusterNodeSSHInfo\": null,\n" +
                "  \"restoreConfig\": null\n" +
                "}";
        Map res = http.sendPost(buildFromPath("/v1/tool/restore"),body);

        String processId = String.valueOf(res.get("response"));
        System.out.println(processId);
        //get process status
        Map res2;
        String status;
        do{
            res2 = http.sendGet(buildFromPath("/v1/tool/restore"), "process="+processId);
            status = res2.get("response").toString();
            System.out.println(res2);
        }while(status.equals("{\"status\":\"processing\"}"));
    }
    @Test
    public void restoreProcessStatusTest() throws Exception {
        http.sendGet(buildFromPath("/v1/tool/restore/5590567581940002998"));
    }
}
