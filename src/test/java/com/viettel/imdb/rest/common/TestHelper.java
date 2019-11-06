package com.viettel.imdb.rest.common;

import com.viettel.imdb.rest.common.HttpResponse;
import com.viettel.imdb.rest.common.TestUtil;
import org.testng.Assert;

import java.util.List;

import static com.viettel.imdb.rest.common.Common.*;


public class TestHelper extends TestUtil {
    static {
        NEED_AUTHORIZE = true;
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
}

