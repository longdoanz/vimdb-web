package com.viettel.imdb.rest.common;

/**
 * @author longdt20
 * @since 17:01 15/02/2019
 */

public class Common {
    public static final String DB_PATH = "namespace";
    public static final String LOGIN_PATH = "/v1/auth/login";
    public static final String CLUSTER_PATH = "/v1/cluster";
    public static final String DATA_PATH = "/v1/data";
    public static final String DEFAULT_NAMESPACE = "namespace";
    public static final String DATA_PATH_WITH_NAMESPACE = DATA_PATH + "/" + DEFAULT_NAMESPACE;
    public static final String AUTH_PATH = "/v1/auth";
    public static final String SECURITY_PATH = "/v1/security";
    public static final String STATISTIC_PATH = "/v1/statistic";
    public static final String METRIC_PATH = "/v1/statistic/metrics";
    public static final String UDF_PATH = "/v1/udf";
    public static final String BACKUP_PATH = "/v1/tool/backup";
    public static final String RESTORE_PATH = "/v1/tool/restore";
    public static final String ADD_NODE = "/v1/cluster/add_node";
    public static final String REMOVE_NODE = "/v1/cluster/remove_node";
    public static final String INDEX_PATH = "indexes";
    public static final String HOST_URL = "http://127.0.0.1:8080";



    public static String USERNAME = "admin";
    public static String PASSWORD = "admin";

    public static String buildFromPath(String... args) {
        String res = "";
        for(String path : args) res += path + "/";
        return res.substring(0, res.length() - 1);
    }
}
