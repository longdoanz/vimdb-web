package com.viettel.imdb.rest.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.openhft.chronicle.core.annotation.NotNull;
import org.testng.Assert;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static com.viettel.imdb.rest.common.Common.LOGIN_PATH;

/**
 * @author longdt20
 * @since 09:18 15/02/2019
 */

public class HTTPRequest {
    private String host;
    private String token;
    private ObjectMapper mapper;

    private void applyHost(String host) {
        allowMethods("PATCH");

        mapper = new ObjectMapper();
        token = "";

        this.host = host.startsWith("http") ? host : "http://" + host;
        if (!this.host.endsWith("/")) {
            this.host = this.host + "/";
        }
    }

    public HTTPRequest(@NotNull String host) {
        applyHost(host);
    }

    public HTTPRequest(@NotNull String host, String username, String password) throws Exception {
        applyHost(host);
        this.authorize(username, password);
    }

    public void authorize(String username, String password) throws Exception {
        String body = "{\n" +
                "  \"username\": \""+username+"\",\n" +
                "  \"password\": \""+password+"\"\n" +
                "}";
        HttpResponse res = this.sendPost(LOGIN_PATH, body);
        try {
            if(res.getResponse().get("token") == null) {
                Assert.fail("UNAUTHORIZED");
            }
            this.token = "Bearer " + res.getResponse().get("token").asText();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("UNAUTHORIZED");
        }
    }

//    public void authorize(String username, String password) throws Exception {
//        URL obj = new URL(buildUri(LOGIN_PATH));
//        HttpURLConnection http = (HttpURLConnection) obj.openConnection();
//        http.setRequestMethod("POST");
//
//        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        http.setRequestProperty("Accept", "application/json");
//
//        http.setDoOutput(true);
//        OutputStream dataOut = http.getOutputStream();
//        dataOut.write(("username="+username+"&password="+password).getBytes());
//        dataOut.flush();
//        dataOut.close();
//        System.out.println("1. "+username+" "+password);
//
//        int res = http.getResponseCode();
//
//        this.token = http.getHeaderField("Set-Cookie");
//        http.disconnect();
//        System.out.println("2. "+username+" "+password);
//    }
//
//    public String getToken() {
//        return this.token;
//    }


    private String buildUri(String path) {
        return this.host + (path.startsWith("/") ? path.substring(1) : path);
    }

    private String buildUri(String path, String queryString) {
        String uri = this.host + (path.startsWith("/") ? path.substring(1) : path);
        if (queryString.isEmpty()) {
            return uri;
        }
        return uri + "?" + queryString;
    }

    private HttpResponse sendWithoutData(String method, String path, String filter) throws Exception {
        return sendWithoutData(method, path, filter, this.token);
    }

    private HttpResponse sendWithoutData(String method, String path, String filter, String token) throws Exception {
        URL obj = new URL(buildUri(path, filter));
        HttpURLConnection http = (HttpURLConnection) obj.openConnection();
        http.setRequestMethod(method);
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Accept-Language", "en");
//        http.setRequestProperty("Cookie", cookie);
        http.setRequestProperty("Authorization", token);

        int code = http.getResponseCode();
        System.out.println("Send: " + method + " " + buildUri(path, filter));
        System.out.println("ResponseCode: " + code);
        InputStream in;
        try {
            in = http.getInputStream();
        } catch (IOException ioex) {
            in = http.getErrorStream();
            if (in == null) {
                ioex.printStackTrace();
                return new HttpResponse(code);
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
        return new HttpResponse(code, response.toString());
    }

    public HttpResponse sendWithData(String method, String path, String body) throws Exception {
        return sendWithData(method, path, null, body, this.token);
    }

    public HttpResponse sendWithData(String method, String path, Map<String, String> header, String body) throws Exception {
        return sendWithData(method, path, header, body, this.token);
    }

    // HTTP request
    public HttpResponse sendWithData(String method, String path, Map<String, String> header, String body, String token) throws Exception {
        URL obj = new URL(buildUri(path));
        HttpURLConnection http = (HttpURLConnection) obj.openConnection();
        http.setRequestMethod(method);
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Accept-Language", "en");
        if(token != null) {
            http.setRequestProperty("Authorization", token);
        }

        if(header != null)
            for (String key : header.keySet()) {
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
            if (in == null)
                return new HttpResponse(resCode);
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));

        String inLine;
        StringBuilder response = new StringBuilder();
        while ((inLine = buffer.readLine()) != null) {
            response.append(inLine);
        }

        buffer.close();
        http.disconnect();
        return new HttpResponse(resCode, response.toString());
    }

    // HTTP GET request
    public HttpResponse sendGet(String path) throws Exception {
        return sendWithoutData("GET", path, "");
    }

    public HttpResponse sendGetWithToken(String path, String token) throws Exception {
        return sendWithoutData("GET", path, "", token);
    }

    public HttpResponse sendGet(String path, String filter) throws Exception {
        return sendWithoutData("GET", path, filter, null);
    }

    public HttpResponse sendGetWithToken(String path, String filter, String token) throws Exception {
        return sendWithoutData("GET", path, filter, token);
    }

/*    public HttpResponse sendGetwithBody(String path, String body) throws Exception {
        return sendWithData("GET", path, new HashMap<String, String>() {
            {
                put("Cookie", token);
                put("Authorization", "admin-admin");
            }
        }, body);
    }*/


    // HTTP POST request
    public HttpResponse sendPost(String path, String body) throws Exception {

        return sendWithData("POST", path, body);
    }

    // HTTP POST request
    public HttpResponse sendPost(String path, String body, String token) throws Exception {
        return sendWithData("POST", path, new HashMap<String, String>() {{
            put("Authorization", token);
        }}, body);
    }

    // HTTP PUT request
    public HttpResponse sendPut(String path, String body) throws Exception {
        return sendWithData("PUT", path, body);
    }

    // HTTP PUT request
    public HttpResponse sendPut(String path, String body, String token) throws Exception {

        return sendWithData("PUT", path, new HashMap<String, String>() {{
            put("Authorization", token);
        }}, body);
    }

    // HTTP PUT request
    public HttpResponse sendPatch(String path, String body) throws Exception {

        return sendWithData("PATCH", path, new HashMap<String, String>() {{
            put("X-HTTP-Method-Override", "PATCH");
        }}, body);
    }

    public HttpResponse sendPatch(String path, String body, String token) throws Exception {

        return sendWithData("POST", path, new HashMap<String, String>() {{
            put("X-HTTP-Method-Override", "PATCH");
            put("Authorization", token);
        }}, body);
    }

    // HTTP DELETE request
    public HttpResponse sendDelete(String path) throws Exception {
        return sendWithoutData("DELETE", path, "");
    }

    public HttpResponse sendDeleteWithToken(String path, String token) throws Exception {
        return sendWithoutData("DELETE", path, "", token);
    }

    public HttpResponse sendDelete(String path, String param) throws Exception {
        return sendWithoutData("DELETE", path, param);
    }

    public HttpResponse sendDeleteWithToken(String path, String param, String token) throws Exception {
        return sendWithoutData("DELETE", path, param, token);
    }

    public String getToken() {
        return token;
    }

    public void allowMethods(String methods) {
        try {
            Field methodsField =
                    HttpURLConnection.class.getDeclaredField("methods");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(methodsField,
                    methodsField.getModifiers() & ~Modifier.FINAL);
            methodsField.setAccessible(true);
            String[] oldMethods = (String[])methodsField.get(null);
            Set<String> methodsSet =
                    new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodsSet.addAll(Arrays.asList(methods));
            String[] newMethods = methodsSet.toArray(new String[0]);
            methodsField.set(null, /*static field*/newMethods);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
