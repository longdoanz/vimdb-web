package com.viettel.imdb.rest.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.openhft.chronicle.core.annotation.NotNull;
import org.springframework.web.util.UriUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author longdt20
 * @since 09:18 15/02/2019
 */

public class HTTPRequest {
    private String host;
    private String cookie;
    private ObjectMapper mapper;

    private void applyHost(String host) {
        mapper = new ObjectMapper();
        cookie = "";

        this.host = host.startsWith("http") ? host : "http://" + host;
        if (!this.host.endsWith("/")) {
            this.host = this.host + "/";
        }
    }

    public HTTPRequest(@NotNull String host){
        applyHost(host);
    }

    public HTTPRequest(@NotNull String host, String username, String password) throws Exception {
        applyHost(host);
        this.authorize(username, password);
    }

    public void authorize(String username, String password) throws Exception {
        URL obj = new URL(buildUri("authenticate"));
        HttpURLConnection http = (HttpURLConnection) obj.openConnection();
        http.setRequestMethod("POST");

        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        http.setRequestProperty("Accept", "*/*");

        http.setDoOutput(true);
        OutputStream dataOut = http.getOutputStream();
        dataOut.write(("username="+username+"&password="+password).getBytes());
        dataOut.flush();
        dataOut.close();
        System.out.println("1. "+username+" "+password);

        int res = http.getResponseCode();
        /*if (res != 200) {
            throw new Exception("LOGIN FAIL");
        }*/

        this.cookie = http.getHeaderField("Set-Cookie");
        http.disconnect();
        System.out.println("2. "+username+" "+password);
    }

    public String getCookie() {
        return this.cookie;
    }



    private String buildUri(String path) {
        return this.host + (path.startsWith("/") ? path.substring(1) : path);

    }

    private String buildUri(String path, String queryString)  {
        String uri = this.host + (path.startsWith("/") ? path.substring(1) : path);
        if(queryString.isEmpty()) {
            return uri;
        }
        return uri + "?" + queryString;

    }

    private Map<String, Object> sendWithoutData(String method, String path, String filter, String token) throws Exception {
        URL obj = new URL(buildUri(path, filter));
        HttpURLConnection http = (HttpURLConnection) obj.openConnection();
        http.setRequestMethod(method);
//        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Cookie", cookie);
        http.setRequestProperty("Authorization", token);

        int code = http.getResponseCode();
        System.out.println("Send: " + method + " " + buildUri(path, filter));
        System.out.println("ResponseCode: " + code);
        InputStream in;
        try {
            in = http.getInputStream();
        } catch (IOException ioex) {
            in = http.getErrorStream();
            if(in == null) {
                return new HashMap<String, Object>(){{
                    put("code", code);
                    put("response", ioex.getMessage());
                }};
            }
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
        String inLine;
        StringBuilder response = new StringBuilder();
        while ((inLine = buffer.readLine()) != null) {
            response.append(inLine);
        }

        buffer.close();
        http.disconnect();
        return new HashMap<String, Object>(){{
            put("code", code);
            put("response", mapper.readTree(response.toString()));
        }};

    }

    // HTTP request
    public Map<String, Object> sendWithData(String method, String path, Map<String, String> header, String body) throws Exception {
        URL obj = new URL(buildUri(path));
        HttpURLConnection http = (HttpURLConnection) obj.openConnection();
        http.setRequestMethod(method);

        for(String key : header.keySet()) {
            http.setRequestProperty(key, header.get(key));
        }

        http.setDoOutput(true);
        OutputStream dataOut = http.getOutputStream();
        dataOut.write(body.getBytes());
        dataOut.flush();
        dataOut.close();

        int resCode = http.getResponseCode();

        System.out.println("Send: " + method + " " + buildUri(path) + ", BODY: " + body);
        System.out.println("ResponseCode: " + resCode);

        InputStream in;
        try {
            in = http.getInputStream();
        } catch (IOException ioex) {
            in = http.getErrorStream();
            if(in == null) {
                return new HashMap<String, Object>(){{
                    put("code", resCode);
                    put("response", ioex.getMessage());
                }};
            }
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));

        String inLine;
        StringBuilder response = new StringBuilder();
        while((inLine = buffer.readLine()) != null) {
            response.append(inLine);
        }

        buffer.close();
        http.disconnect();
        return new HashMap<String, Object>(){{
            put("code", resCode);
            put("response", mapper.readTree(response.toString()));
        }};

    }

    // HTTP GET request
    public Map<String, Object> sendGet(String path) throws Exception {
        return sendWithoutData("GET", path, "", null);
    }
    public Map<String, Object> sendGetWithToken(String path, String token) throws Exception {
        return sendWithoutData("GET", path, "", token);
    }

    public Map<String, Object> sendGet(String path, String filter) throws Exception {
        return sendWithoutData("GET", path, filter, null);
    }
    public Map<String, Object> sendGetWithToken(String path, String filter, String token) throws Exception {
        return sendWithoutData("GET", path, filter, token);
    }

    public Map<String, Object> sendGetwithBody(String path, String body) throws Exception {
        return sendWithData("GET", path, new HashMap<String, String>(){
            {
                put("Content-Type", "application/json");
                put("Accept", "application/json");
                put("Cookie", cookie);
                put("Authorization", "admin-admin");
            }
        }, body);
    }


    // HTTP POST request
    public Map<String, Object> sendPost(String path, String body) throws Exception {

        return sendWithData("POST", path, new HashMap<String, String>() {{
            put("Content-Type", "application/json");
            put("Accept", "application/json");
            put("Cookie", cookie);
            //put("Authorization", "admin-admin");
        }}, body);
    }
    // HTTP POST request
    public Map<String, Object> sendPost(String path, String body, String token) throws Exception {

        return sendWithData("POST", path, new HashMap<String, String>() {{
            put("Content-Type", "application/json");
            put("Accept", "application/json");
            put("Cookie", cookie);
            put("Authorization", token);
        }}, body);
    }

    // HTTP PUT request
    public Map<String, Object> sendPut(String path, String body) throws Exception {

        return sendWithData("PUT", path, new HashMap<String, String>() {{
            put("Content-Type", "application/json");
            put("Accept", "application/json");
            put("Cookie", cookie);
        }}, body);
    }
    // HTTP PUT request
    public Map<String, Object> sendPut(String path, String body, String token) throws Exception {

        return sendWithData("PUT", path, new HashMap<String, String>() {{
            put("Content-Type", "application/json");
            put("Accept", "application/json");
            put("Cookie", cookie);
            put("Authorization", token);
        }}, body);
    }
    // HTTP PUT request
    public Map<String, Object> sendPatch(String path, String body) throws Exception {

        return sendWithData("PATCH", path, new HashMap<String, String>() {{
            put("Content-Type", "application/json");
            put("Accept", "application/json");
            put("Cookie", cookie);
        }}, body);
    }
    public Map<String, Object> sendPatch(String path, String body, String token) throws Exception {

        return sendWithData("PATCH", path, new HashMap<String, String>() {{
            put("Content-Type", "application/json");
            put("Accept", "application/json");
            put("Cookie", cookie);
            put("Authorization", token);
        }}, body);
    }

    // HTTP DELETE request
    public Map<String, Object> sendDelete(String path) throws Exception {
        return sendWithoutData("DELETE", path, "",null);
    }
    public Map<String, Object> sendDeleteWithToen(String path, String token) throws Exception {
        return sendWithoutData("DELETE", path, "",token);
    }
    public Map<String, Object> sendDelete(String path, String filter) throws Exception {
        return sendWithoutData("DELETE", path, filter, null);
    }
    public Map<String, Object> sendDeleteWithToken(String path, String filter, String token) throws Exception {
        return sendWithoutData("DELETE", path, filter, token);
    }
}
