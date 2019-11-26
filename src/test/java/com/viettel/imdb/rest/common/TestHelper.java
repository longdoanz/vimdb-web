package com.viettel.imdb.rest.common;

import org.testng.Assert;

import java.util.List;

import static com.viettel.imdb.rest.common.Common.*;


public class TestHelper extends TestUtil {
    static {
        NEED_AUTHORIZE = false;
    }


    public HttpResponse getRole(String... roleName) {
        String path = "";
        if(roleName.length != 0) {
            path = "/" + roleName[0];
        }
        try {
            return http.sendGet(SECURITY_PATH + "/role" + path);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse createRole(String body) {
        try {
            return http.sendPost(SECURITY_PATH + "/role", body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse updateRole(String roleName, String body) {
        try {
            return http.sendPatch(SECURITY_PATH + "/role/" + roleName, body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse dropRole(String roleName) {
        try {
            return http.sendDelete(SECURITY_PATH + "/role/" + roleName);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }


    public HttpResponse createUser(String body) {
        try {
            return http.sendPost(SECURITY_PATH + "/user", body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse getUser(String... username) {
        String path = "";
        if(username.length != 0) {
            path = "/" + username[0];
        }
        try {
            return http.sendGet(SECURITY_PATH + "/user" + path);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse updateUser(String username, String body) {
        try {
            return http.sendPatch(SECURITY_PATH + "/user/" + username, body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse dropUser(String username) {
        try {
            return http.sendDelete(SECURITY_PATH + "/user/" + username);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }


    public HttpResponse createUdf(String udfName, String body) {
        try {
            return http.sendPost(UDF_PATH + "/" + udfName, body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse getUdf(String... udfName) {
        String path = "";
        if(udfName.length != 0) {
            path = "/" + udfName[0];
        }
        try {
            return http.sendGet(UDF_PATH + path);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse updateUdf(String udfName, String body) {
        try {
            return http.sendPatch(UDF_PATH + "/" + udfName, body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse dropUdf(String udfName) {
        try {
            return http.sendDelete(UDF_PATH + "/" + udfName);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }


    public HttpResponse getStatistic() {
        return getStatistic(null, null);
    }

    public HttpResponse getStatistic(List<String> nodes, List<String> metrics) {
        String param = "";
        if(nodes != null && !nodes.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            int index = 0;
            for(String item : nodes) {
                builder.append(item).append(",");
            }
            String temp = builder.toString();
            param = "?nodes=" + temp.substring(0, temp.length() - 1);
        }

        if(metrics != null && !metrics.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for(String item : metrics)
                builder.append(item).append(",");
            if(param.length() == 0) {
                param += "?";
            } else {
                param += "&";
            }
            String temp = builder.toString();
            param += "metrics=" + temp.substring(0, temp.length() - 1);
        }

        try {
            return http.sendGet(STATISTIC_PATH + param);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse getMetrics() {
        try {
            return http.sendGet(STATISTIC_PATH);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }


    //data

    public HttpResponse getData(String... nameSpace) {
        String path = "";
        if(nameSpace.length != 0) {
            path = "/" + nameSpace[0];
        }
        try {
            return http.sendGet(DATA_PATH + path);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse createTable(String tableName) {
        String body = "{\n" +
                "  \"tableName\": \""+tableName+"\"\n" +
                "}";;
        try {
            return http.sendPost(DATA_PATH + "/namespace", body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse dropTable(String tableName) {
        try {
            return http.sendDelete(DATA_PATH_WITH_NAMESPACE + "/" + tableName);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse getRecord(String tableName, String key) {
        try {
            return http.sendGet(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, key));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse createRecord(String tableName, String key, String body) {
        try {
            return http.sendPost(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, key), body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse updateRecord(String tableName, String key, String body) {
        try {
            return http.sendPatch(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, key), body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse dropRecord(String tableName, String key) {
        try {
            return http.sendDelete(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, key));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse findRecordPK( String tableName, String ...key) {
        try {
            return http.sendGet(DATA_PATH + "/namespace/"+ tableName +"/"+ key);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse findRecordSK( int minRange, int maxRange) // ham chua viet
    {
        try {
            return http.sendGet(DATA_PATH + "/namespace/");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }
    public HttpResponse addNode(String body){
        try {
            return http.sendPost(ADD_NODE, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public HttpResponse removeNode(String body){
        try {
            return http.sendPost(REMOVE_NODE, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

