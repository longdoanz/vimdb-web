package com.viettel.imdb.rest;

import com.viettel.imdb.rest.common.HTTPRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
    public void getUDFsList2() throws Exception {
        http.sendGetWithToken(buildFromPath("/v1/udf/"), "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTU3MTcyNzc5OCwiZXhwIjoxNTcxNzI4Njk4fQ.C1Dkl4CIMsK3DF59c7-E9Oa-p4P9JZFk9bAxZCmKJRBvl9GGgYu1Dw1ct5e8-v_D7jE4LVGK0cuP8Z_vJo93tg");
    }
    @Test
    public void getinsertUDF() throws Exception {
        for(int i = 0; i < 10; i++){
            String body = "{\n" +
                    "  \"fileName\": \"thurv"+i+"\",\n" +
                    "  \"type\": \"LUA\",\n" +
                    "  \"syncedOnAllNodes\": true,\n" +
                    "  \"content\": \"abzxyz\"\n" +
                    "}";
            Map res = http.sendPost(buildFromPath("/v1/udf/thurv"+i), body);
            System.out.println(res);
        }

    }
    @Test
    public void getinsertUDF2() throws Exception {
        for(int i = 0; i < 10; i++){
            String body = "{\n" +
                    "  \"fileName\": \"thurv"+i+"\",\n" +
                    "  \"type\": \"LUA\",\n" +
                    "  \"syncedOnAllNodes\": true,\n" +
                    "  \"content\": \"abzxyz\"\n" +
                    "}";
            Map res = http.sendPost(buildFromPath("/v1/udf/thurv"+i), body, "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTU3MTcyNzc5OCwiZXhwIjoxNTcxNzI4Njk4fQ.C1Dkl4CIMsK3DF59c7-E9Oa-p4P9JZFk9bAxZCmKJRBvl9GGgYu1Dw1ct5e8-v_D7jE4LVGK0cuP8Z_vJo93tg");
            System.out.println(res);
        }

    }
    @Test
    public void editUDF() throws Exception {
        String body = "{\n" +
                "  \"fileName\": \"thurv4.lua\",\n" +
                "  \"type\": null,\n" +
                "  \"syncedOnAllNodes\": true,\n" +
                "  \"content\": \"ABCXYZ\"\n" +
                "}";
        Map res = http.sendPut(buildFromPath("/v1/udf/thurv4"), body);
        System.out.println(res);
    }
    @Test
    public void delete() throws Exception {
        http.sendDelete(buildFromPath("/v1/udf/thurv2"));
    }
    @Test
    public void timerTest() throws Exception {
        //http.sendDelete(buildFromPath("/v1/udf/thurv2"));
        class MyTask extends TimerTask {
            @Override
            public void run() {
                System.out.println("Run my Task " + new Date());
            }
        }
        MyTask myTask = new MyTask();
        Timer timer = new Timer();
        System.out.println("Currnet time: " + new Date());
        timer.schedule(myTask, 0, 2000);
        System.out.println("anh thu");
    }

}
