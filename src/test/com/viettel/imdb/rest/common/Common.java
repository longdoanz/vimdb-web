package com.viettel.imdb.rest.common;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author longdt20
 * @since 17:01 15/02/2019
 */

public class Common {
    public static final String DB_PATH = "default";
    public static final String DATA_PATH = "v1/data";
    public static final String DEFAULT_NAMESPACE = "namespace";
    public static final String DATA_PATH_WITH_NAMESPACE = DATA_PATH + "/" + DEFAULT_NAMESPACE;
    public static final String INDEX_PATH = "indexes";
    public static final String HOST_URL = "http://127.0.0.1:8080";

    public static String USERNAME = "superadmin";

    public static String PASSWORD = "superadminpass";

    public static String buildFromPath(String... args) {
        String res = "";
        for(String path : args) res += path + "/";
        return res.substring(0, res.length() - 1);
    }
}
