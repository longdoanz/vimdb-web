package com.viettel.imdb.rest.common;

import org.springframework.http.HttpStatus;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.viettel.imdb.rest.common.Common.*;


public class TestHelper extends TestUtil {
    static {
        NEED_AUTHORIZE = false;
    }


    public <T> List<T> toArrayList(T... args) {
        return Arrays.asList(args);
    }

    public String generateString(char a, int number) {
        return new String(new char[number]).replace('\0', a);
    }

    public String updateClusterConfigPort(String config, int port) {
        return config.replaceAll("\"port\".*\\d *,", String.format("\"port\":%d,", port));
    }

    public String getClusterSeeds(String... additionHosts) {
        HttpResponse response = getClusterInfo().andExpect(HttpStatus.OK);
        List<String> nodes = response.read("nodes.[*].ip");
        List<Integer> ports = response.read("nodes.[*].port");

        StringBuilder builder = new StringBuilder("[");
        for(int i = 0; i < nodes.size(); i++) {
            builder.append("\\\\\"").append(nodes.get(i)).append(":").append(ports.get(i)).append("\\\\\"\\\\n");
        }
        for(String host: additionHosts) {
            builder.append("\\\\\"").append(host).append("\\\\\"\\\\n");
        }
        builder.append("]");
        System.err.println(builder.toString());
        return builder.toString();

    }

    public HttpResponse getRole(String... roleName) {
        String path = "";
        if (roleName.length != 0) {
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
        if (username.length != 0) {
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
        if (udfName.length != 0) {
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
        if (nodes != null && !nodes.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            int index = 0;
            for (String item : nodes) {
                builder.append(item).append(",");
            }
            String temp = builder.toString();
            param = "?servers=" + temp.substring(0, temp.length() - 1);
        }

        if (metrics != null && !metrics.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String item : metrics)
                builder.append(item).append(",");
            if (param.length() == 0) {
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
            return http.sendGet(METRIC_PATH);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse backup(String body) {
        try {
            return http.sendPost(BACKUP_PATH, body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse restore(String body) {
        try {
            return http.sendPost(RESTORE_PATH, body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse backupRestoreStatus(String path) {
        try {
            return http.sendGet(path);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse getClusterInfo() {
        try {
            return http.sendGet("/v1/cluster/info");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpResponse addNode(String body) {
        try {
            return http.sendPost(ADD_NODE, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpResponse removeNode(String body) {
        try {
            return http.sendPost(REMOVE_NODE, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //data

    public HttpResponse getData(String... nameSpace) {
        String path = "";
        if (nameSpace.length != 0) {
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
                "  \"tableName\": \"" + tableName + "\"\n" +
                "}";
        try {
            return http.sendPost(DATA_PATH + "/namespace", body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse dropTable(String tableName) {
        try {
            Thread.sleep(1000);
            return http.sendDelete(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName));
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

    public HttpResponse getRecords(String tableName) {
        try {
            return http.sendGet(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName));
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

    private static String convertToStringWithEscapeCharacters(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c == '"') {
                stringBuilder.append("\\");
            } else if(c == '\n') {
                stringBuilder.append("\\\n");
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public HttpResponse runCmd(String cmd) {
        String body = "{\n" +
                "\"cmd\": \"" + convertToStringWithEscapeCharacters(cmd) + "\"" +
                "\n}";
        System.out.println(body);
        try {
            return http.sendPost(DATA_PATH + "/cmd", body);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public void waitAllDone(List<Runnable> tasks) {
        ExecutorService es = Executors.newCachedThreadPool();
        try {
            List<java.util.concurrent.Future> futureList = new ArrayList<>();
            for (Runnable task : tasks) {
                futureList.add(es.submit(task));
            }
            for(java.util.concurrent.Future future : futureList)
                future.get();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail of some tasks!!!!!!!!!!!!!");
        }
    }


    public HttpResponse findRecordPK(String tableName, String key) {
        try {
            return http.sendGet(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName, key));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public HttpResponse findRecordSK(int minRange, int maxRange) // ham chua viet
    {
        try {
            return http.sendGet(buildFromPath(DATA_PATH_WITH_NAMESPACE));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    public boolean waitNodeToAdded(String waitingIp, int port) {
        long startTime = System.currentTimeMillis();
        // Waiting for 30 second
        while ((System.currentTimeMillis() - startTime) < 30000) {
            HttpResponse response = getClusterInfo();
            if (response.getStatus() != HttpStatus.OK)
                return false;
            System.out.println("Response: " + response);
            List<String> hosts = response.read("nodes.[*].ip");
            List<Integer> ports = response.read("nodes.[*].port");

            if (hosts.contains(waitingIp) && ports.contains(port))
                return true;
            System.out.println("--- ON WAITING GET NODE ---");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
        }
        return false;
    }

}

