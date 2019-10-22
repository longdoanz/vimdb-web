package com.viettel.imdb.rest;

import com.viettel.imdb.rest.common.HTTPRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static com.viettel.imdb.rest.common.Common.*;

public class DataTest {
    private HTTPRequest http;
    @BeforeMethod
    public void setUp() throws Exception {
        http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);
    }
    @Test
    public void httpCookie() {
        System.out.println(http.getCookie());
    }

    @Test void getDataInfitest() throws Exception {
        Map res = http.sendGet(buildFromPath("/v1/data/a"));
    }

}
