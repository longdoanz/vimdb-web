package com.viettel.imdb.rest.common;

import org.yaml.snakeyaml.util.UriEncoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author longdt20
 * @since 11:01 21/02/2019
 */

public class QueryParam {
    private String filter;
    private String fields;
    private int limit;

//    Use to add optional query param
    private String extend;

    public QueryParam() {
        this.extend = "";
    }

    public QueryParam(String filter) {
        this.extend = "";
        this.filter = filter;
    }

    public QueryParam(String filter, String fields, int limit) {
        this.filter = filter;
        this.fields = fields;
        this.limit = limit;
        this.extend = "";
    }

    public QueryParam(String filter, String fields) {
        this.filter = filter;
        this.fields = fields;
        this.extend = "";
    }

    public void addParam(String name, String value) {
        this.extend += "&" + name + "=" + value;
    }

    public void clearParam() {
        this.extend = "";
    }

    public String getQuery() {
        String res = "";
        if(filter != null && !filter.isEmpty()) {
            try {
                res += "filter=" + URLEncoder.encode(this.filter, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if(fields != null && !fields.isEmpty()) {
            res += (res.isEmpty() ? "":"&") + "fields=" + UriEncoder.encode(this.fields);
        }

        if(this.limit != 0) {
            res += (res.isEmpty() ? "":"&") + "limit=" + this.limit;
        }

        return res;
    }
}
