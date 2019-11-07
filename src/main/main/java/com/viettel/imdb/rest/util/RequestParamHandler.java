package com.viettel.imdb.rest.util;

import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author quannh22
 * @since 09/08/2019
 */
public class RequestParamHandler {
    public static List<String> getFieldNameListFromMap(MultiValueMap<String, String> requestParams) {
        List<String> fieldNameList = requestParams.get(VIMDBRestConstant.FIELD_NAME_LIST);
        if(fieldNameList == null)
            return new ArrayList<>();
        return fieldNameList;
    }
}
