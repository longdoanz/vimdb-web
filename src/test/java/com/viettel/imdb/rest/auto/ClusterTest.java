package com.viettel.imdb.rest.auto;

import com.viettel.imdb.rest.common.TestHelper;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

public class ClusterTest extends TestHelper {

    @Test(priority = 2)
    public void testAddNode() {
        String body = "{\n" +
                "  \"sshInfo\": {\n" +
                "    \"authenticationOption\": \"none\",\n" +
                "    \"ip\": \"172.16.28.123\",\n" +
                "    \"password\": \"imdb\",\n" +
                "    \"port\": 22,\n" +
                "    \"sshKey\": \"admind\",\n" +
                "    \"username\": \"imdb\"\n" +
                "  },\n" +
                "  \"vimdbServerInfo\": {\n" +
                "    \"binary\": \"/home/imdb/longdt/vimdb2/release/apps/vimdb_with_security\",\n" +
                "    \"configFile\": \"/home/imdb/longdt/vimdb2/release/apps/vimdb_thurvnode0.toml\",\n" +
                "    \"defaultBinary\": false,\n" +
                "    \"defaultConfigFile\": false,\n" +
                "    \"newConfigContent\": \"cluster_name = \\\"vIMDB Cluster\\\"\\nhost = \\\"172.16.28.123\\\"\\nport = 29050\\nreplication_factor = 3\\nmetric_port = 11111\\nseeds = [\\n  \\\"172.16.28.123:29050\\\"\\n  \\\"172.16.28.123:29051\\\"\\n  \\\"172.16.28.123:14080\\\"\\n]\\ndefault_log_level = \\\"info\\\"\\ncluster_backup_log = \\\"thurv_cluster0.log\\\"\\nbackup_data_directory = \\\"thurv_db0\\\"\\n[log_level]\\nvimdb = \\\"error\\\"\"\n" +
                "  }\n" +
                "}";
        ;
        addNode(body).andExpect(HttpStatus.ACCEPTED);
    }

    @Test(priority = 2)
    public void testRemoveNode() {
        String body = "{\n" +
                "  \"host\": \"172.16.28.123\",\n" +
                "  \"port\": 29050,\n" +
                "  \"password\": \"admin\",\n" +
                "  \"username\": \"admin\"\n" +
                "}";
        removeNode(body).andExpect(HttpStatus.ACCEPTED);
    }

}
