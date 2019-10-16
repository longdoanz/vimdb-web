package com.viettel.imdb.rest.common;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author longdt20
 * @since 17:01 15/02/2019
 */

public class Common {
    public static final String DB_PATH = "namespace";
    public static final String CLUSTER_PATH = "v1/cluster";
    public static final String DATA_PATH = "v1/data";
    public static final String DEFAULT_NAMESPACE = "namespace";
    public static final String DATA_PATH_WITH_NAMESPACE = DATA_PATH + "/" + DEFAULT_NAMESPACE;
    public static final String AUTH_PATH = "v1/auth";
    public static final String SECURITY_PATH = "v1/security";
    public static final String STATISTIC_PATH = "v1/statistic";
    public static final String INDEX_PATH = "indexes";
    public static final String HOST_URL = "https://vimdb-web.herokuapp.com";

    public static String USERNAME = "superadmin";

    public static String PASSWORD = "superadminpass";

    public static String buildFromPath(String... args) {
        String res = "";
        for(String path : args) res += path + "/";
        return res.substring(0, res.length() - 1);
    }
}
