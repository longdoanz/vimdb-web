package com.viettel.imdb.rest.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.imdb.core.security.User;
import com.viettel.imdb.rest.domain.RestIndexModel;
import com.viettel.imdb.rest.model.AddUserRequest;
import com.viettel.imdb.util.IMDBEncodeDecoder;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static com.viettel.imdb.rest.common.Common.*;
import static org.testng.Assert.assertEquals;

/**
 * @author quannh22
 * @since 03/09/2019
 */
public class TestUtil {
    public HTTPRequest http = new HTTPRequest(HOST_URL);;
    private IMDBEncodeDecoder encoder;
    @BeforeMethod
    public void setUp() throws Exception {
        http = new HTTPRequest(HOST_URL);
        encoder = IMDBEncodeDecoder.getInstance();
    }

    public HttpResponse createTable(String tableName) throws Exception {
        return http.sendPost(DATA_PATH_WITH_NAMESPACE, "{\"tableName\":  \"" + tableName + "\"}");
    }

    public HttpResponse dropTable(String tableName) throws Exception {
        return http.sendDelete(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName));
    }

    public HttpResponse insert(String tableName, String key, String body) throws Exception {
        return http.sendPost(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, key), body);
    }

    public HttpResponse select(String tableName, String key) throws Exception {
        return http.sendGet(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, key));
    }

    public HttpResponse update(String tableName, String key, String body) throws Exception {
        return http.sendPatch(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, key), body);
    }

    public HttpResponse delete(String tableName, String key) throws Exception {
        return http.sendDelete(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, key));
    }

    public void testCreateTable(String tableName, HttpStatus expectedReturnCode, String expectedResponse) {
        try {
            HttpResponse res = createTable(tableName);
//            System.out.println(res);
//            Assert.assertEquals(res.getStatus(), expectedReturnCode);

            res.andExpect(expectedReturnCode);
            if(expectedResponse == null)
                Assert.assertNull(res.getResponse());
            else
                res.andExpectResponse(expectedResponse);
//                Assert.assertEquals(encoder.encodeJsonString(res.getResponse().toString()), encoder.encodeJsonString(expectedResponse));
        }catch (Exception ex) {
            Assert.fail("Test create table failed!!!", ex);
        }
    }

    public void testDropTable(String tableName, HttpStatus expectedReturnCode, String expectedResponse) {
        try {
            HttpResponse res = http.sendDelete(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName));

            res.andExpect(expectedReturnCode)
                    .andExpectResponse(expectedResponse);
        }catch (Exception ex) {
            Assert.fail("Test drop table failed!!!", ex);
        }
    }

    public void testCreateIndex(String tableName, String indexName, HttpStatus expectedReturnCode, String expectedResponse) {
        try {
            final String INDEX_TYPE = "numeric";
            RestIndexModel restIndexModel = new RestIndexModel(DEFAULT_NAMESPACE, tableName, INDEX_TYPE, indexName);
            String path = buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName);
            HttpResponse res = http.sendPost(path, restIndexModel.toJsonString());

            res.andExpect(expectedReturnCode);

            if(expectedResponse == null)
                Assert.assertNull(res.getResponse());
            else
                res.andExpectResponse(expectedResponse);
        } catch (Exception ex) {
            Assert.fail("Test create index failed!!!", ex);
        }
    }

    public void testCreateIndexString(String tableName, String indexName, HttpStatus expectedReturnCode, String expectedResponse) {
        try {
            final String INDEX_TYPE = "string";
            RestIndexModel restIndexModel = new RestIndexModel(DEFAULT_NAMESPACE, tableName, INDEX_TYPE, indexName);
            String path = buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName);
            HttpResponse res = http.sendPost(path, restIndexModel.toJsonString());
            System.out.println(res);
            Assert.assertEquals(res.getStatus(), expectedReturnCode);
            if(expectedResponse == null)
                Assert.assertNull(res.getResponse());
            else
                Assert.assertEquals(encoder.encodeJsonString(res.getResponse().toString()), encoder.encodeJsonString(expectedResponse));
        } catch (Exception ex) {
            Assert.fail("Test create index failed!!!", ex);
        }
    }


    public void testCreateIndexWithType(String tableName, String indexName, String type, HttpStatus expectedReturnCode, String expectedResponse) {
        try {
            RestIndexModel restIndexModel = new RestIndexModel(DEFAULT_NAMESPACE, tableName, type, indexName);
            String path = buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName);
            HttpResponse res = http.sendPost(path, restIndexModel.toJsonString());
            System.out.println(res);
            Assert.assertEquals(res.getStatus(), expectedReturnCode);
            if(expectedResponse == null)
                Assert.assertNull(res.getResponse());
            else
                Assert.assertEquals(encoder.encodeJsonString(res.getResponse().toString()), encoder.encodeJsonString(expectedResponse));
        } catch (Exception ex) {
            Assert.fail("Test create index failed!!!", ex);
        }
    }

    public String testLogin(String username, String password, HttpStatus expectedReturnCode, String expectedErrorMessageResponse) {
        String token = null;
        try {
            String body = "{\n" +
                "  \"username\": \""+username+"\",\n" +
                "  \"password\": \""+password+"\"\n" +
                "}";
            //Map res = http.sendGet(AUTH_PATH + "/login", "username=" + username + "&password=" + password);
            HttpResponse res = http.sendPost(AUTH_PATH + "/login", body);

            System.out.println(res);
            Assert.assertEquals(res.getStatus(), expectedReturnCode);
            com.fasterxml.jackson.databind.node.ObjectNode response = (com.fasterxml.jackson.databind.node.ObjectNode) res.getResponse();
            if(expectedErrorMessageResponse != null) {
                String actualErrorMessage = response.get("error").asText();
                Assert.assertEquals(actualErrorMessage, expectedErrorMessageResponse);
            } else {
                token = response.get("token").asText();
            }
        }catch (Exception ex) {
            Assert.fail("login failed!!!", ex);
        }
        return token;
    }
    public String createAuthenticationToken(String username, String password, HttpStatus expectedReturnCode, String expectedErrorMessageResponse) {
        String token = null;
        try {
            String body = "{\n" +
                    "  \"username\": \""+username+"\",\n" +
                    "  \"password\": \""+password+"\"\n" +
                    "}";
            HttpResponse res = http.sendPost(LOGIN_PATH, body);

            System.out.println(res);
            //Assert.assertEquals(res.getStatus(), expectedReturnCode.value());
            if(expectedErrorMessageResponse != null) {
                String actualErrorMessage = res.getResponse().get("error").asText();
                //Assert.assertEquals(actualErrorMessage, expectedErrorMessageResponse);
            } else {
                token = res.getResponse().get("token").asText();
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("login failed!!!");
        }
        return token;
    }


    ///================================================
    /// SECURITY TESTS - BEGIN
    ///================================================


    public void testAddUser(AddUserRequest request, HttpStatus expectedReturnCode, String expectedResponse) {
        // todo expectedResponse can be error or value
        try {
            HttpResponse res = http.sendPost(buildFromPath(SECURITY_PATH, "user", request.getUserName()), new ObjectMapper().writeValueAsString(request));
            System.out.println(res);
            assertEquals(res.getStatus(), expectedReturnCode.value());
            if(expectedResponse == null)
                Assert.assertNull(res.getResponse());
            else
                Assert.assertEquals(
                        encoder.decodeJsonNode(encoder.encodeJsonString(res.getResponse().toString())),
                        encoder.decodeJsonNode(encoder.encodeJsonString(expectedResponse))
                );
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Cannot call getUsers()", ex);
        }
    }

    public void testGetUsers(HttpStatus expectedReturnCode, List<User> userList) {
        // todo expectedResponse can be error or value
        try {
            HttpResponse res = http.sendGet(SECURITY_PATH + "/user");
            System.out.println(res);
            assertEquals(res.getStatus(), expectedReturnCode);
            //JsonNode actualResult = encoder.decodeJsonNode(encoder.encodeJsonString(res.getResponse().toString())).get("results");
            JsonNode actualResult = encoder.decodeJsonNode(encoder.encodeJsonString(res.getResponse().toString()));
            JsonNode expectedResult = encoder.decodeJsonNode(encoder.encodeJsonString(new ObjectMapper().writeValueAsString(userList)));
            //Assert.assertEquals(actualResult, expectedResult);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Cannot call getUsers()", ex);
        }
    }
    public void tesAuditLog() {
        // todo expectedResponse can be error or value
        try {
            HttpResponse res = http.sendGet(SECURITY_PATH + "/audit-log");
            System.out.println(res);
//            assertEquals(res.getStatus(), expectedReturnCode.value());
//            JsonNode actualResult = encoder.decodeJsonNode(encoder.encodeJsonString(res.getResponse().toString())).get("results");
//            JsonNode expectedResult = encoder.decodeJsonNode(encoder.encodeJsonString(new ObjectMapper().writeValueAsString(userList)));
//            Assert.assertEquals(actualResult, expectedResult);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Cannot call tesAuditLog()", ex);
        }
    }
    public void testRole() {
        // todo expectedResponse can be error or value
        try {
            HttpResponse res = http.sendGet(SECURITY_PATH + "/role");
            System.out.println(res);
//            assertEquals(res.getStatus(), expectedReturnCode.value());
//            JsonNode actualResult = encoder.decodeJsonNode(encoder.encodeJsonString(res.getResponse().toString())).get("results");
//            JsonNode expectedResult = encoder.decodeJsonNode(encoder.encodeJsonString(new ObjectMapper().writeValueAsString(userList)));
//            Assert.assertEquals(actualResult, expectedResult);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Cannot call tesAuditLog()", ex);
        }
    }
    ///================================================
    /// SECURITY TESTS - END
    ///================================================


    ///================================================
    /// METRICS TESTS - BEGIN
    ///================================================
    public void testGetMetrics(List<String> serverList, HttpStatus expectedReturnCode, String expectedResponse) {
        // todo expectedResponse can be error or value
        try {
            HttpResponse res;
            String filterStr = "";
            if(serverList!= null && !serverList.isEmpty()) {
                filterStr = "servers=";
                for(String server : serverList) {
                    filterStr += server +",";
                }
                filterStr = filterStr.substring(0, filterStr.length()-1);
            }
            if(filterStr.isEmpty())
                res = http.sendGet(buildFromPath(STATISTIC_PATH, "metrics")/*, "servers="*/);
            else
                res = http.sendGet(buildFromPath(STATISTIC_PATH, "metrics"),filterStr);
            System.out.println(res);
            assertEquals(res.getStatus(), expectedReturnCode.value());
            if(expectedResponse == null)
                Assert.assertNull(res.getResponse());
            else
//                res.andExpect();
                Assert.assertEquals(
                        encoder.decodeJsonNode(encoder.encodeJsonString(res.getResponse().toString())),
                        encoder.decodeJsonNode(encoder.encodeJsonString(expectedResponse))
                );
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Cannot call getUsers()", ex);
        }
    }

    public void testGetStatistic(List<String> serverList, List<String> metricList, HttpStatus expectedReturnCode, String expectedResponse) {
        // todo expectedResponse can be error or value
        try {
            HttpResponse res;
            String serverFilterStr = "";
            if(serverList!= null && !serverList.isEmpty()) {
                serverFilterStr = "servers=";
                for(String server : serverList) {
                    serverFilterStr += server +",";
                }
                serverFilterStr = serverFilterStr.substring(0, serverFilterStr.length()-1);
            }

            String metricFilterStr = "";
            if(metricList!= null && !metricList.isEmpty()) {
                metricFilterStr = "metrics=";
                for(String metric : metricList) {
                    metricFilterStr += metric +",";
                }
                metricFilterStr = metricFilterStr.substring(0, metricFilterStr.length()-1);
            }
            
            
            if(serverFilterStr.isEmpty() && metricFilterStr.isEmpty()) {
                res = http.sendGet(buildFromPath(STATISTIC_PATH));
            } else if(serverFilterStr.isEmpty()) {
                res = http.sendGet(buildFromPath(STATISTIC_PATH), metricFilterStr);
            } else if(metricFilterStr.isEmpty()) {
                res = http.sendGet(buildFromPath(STATISTIC_PATH), serverFilterStr );
            } else {
                res = http.sendGet(buildFromPath(STATISTIC_PATH), serverFilterStr + "&" + metricFilterStr);
            }
            System.out.println(res);
            assertEquals(res.getStatus(), expectedReturnCode);
            if(expectedResponse == null)
                Assert.assertNull(res.getResponse());
            else
                Assert.assertEquals(
                        encoder.decodeJsonNode(encoder.encodeJsonString(res.getResponse().toString())),
                        encoder.decodeJsonNode(encoder.encodeJsonString(expectedResponse))
                );
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Cannot call getUsers()", ex);
        }
    }
    ///================================================
    /// METRICS TESTS - END
    ///================================================

}
