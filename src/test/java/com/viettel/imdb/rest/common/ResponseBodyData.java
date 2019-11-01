package com.viettel.imdb.rest.common;

import com.jayway.jsonpath.JsonPath;

public class ResponseBodyData<T> {
    public <T> T read(String path, Class<T> tClass) {
        return JsonPath.parse(path).read(path, tClass);
    }
}
