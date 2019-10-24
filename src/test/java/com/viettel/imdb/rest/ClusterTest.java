package com.viettel.imdb.rest;

import com.viettel.imdb.rest.common.HTTPRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static com.viettel.imdb.rest.common.Common.*;

public class ClusterTest {
    private HTTPRequest http;
    @BeforeMethod
    public void setUp() throws Exception {
        http = new HTTPRequest(HOST_URL, USERNAME, PASSWORD);
    }
    @Test
    public void httpCookie() {
        System.out.println(http.getToken());
    }

    @Test
    public void clusterGetInfo() throws Exception {
        String body =
                "[\n" +
                " \"172.16.28.121:9669\"\n" +
                "]";


//        String body =
//               "[\n" +
//                        " \"172.16.28.121:9669\"\n" +
//                        " ]";

        System.out.println(body);
        Map res = http.sendGet(buildFromPath("/v1/cluster/info"), "nodes=172.16.28.69:10000,172.16.28.70:10000");
        System.out.println(res);
    }

    @Test
    public void test_Add_New_Node() throws Exception {
        Map res = http.sendPost(CLUSTER_PATH + "/add_node", "{\n" +
                "  \"sshInfo\": {\n" +
                "    \"ip\": \"172.16.28.123\",\n" +
                "    \"password\": \"admin\",\n" +
                "    \"username\": \"admin\"\n" +
                "  },\n" +
                "  \"vimdbServerInfo\": {\n" +
                "    \"privileges\": \"privileges list\",\n" +
                "    \"roleName\": \"string\"\n" +
                "  }\n" +
                "}");
        System.out.println(res);
    }

    @Test
    public void test_Remove_Node() throws Exception {
        Map res = http.sendDelete(CLUSTER_PATH + "/remove_node", "{\n" +
                "  " +
                "\"sshInfo\": {\"ip\": \"172.16.28.129\"},\n" +
                "  \"vimdbServerInfo\": {\n" +
                "    \"host\": \"172.16.28.120\",\n" +
                "    \"port\": 12834\n" +
                "  }\n" +
                "}");
        System.out.println(res);
    }

    @Test
    public void delete() throws Exception {
        String body =
                "[\n" +
                        " \"172.16.28.121:9669\"\n" +
                        "]";


//        String body =
//               "[\n" +
//                        " \"172.16.28.121:9669\"\n" +
//                        " ]";

        System.out.println(body);
        Map res = http.sendGetwithBody(buildFromPath("/v1/cluster/info"), body);
        System.out.println(res);
    }

}
