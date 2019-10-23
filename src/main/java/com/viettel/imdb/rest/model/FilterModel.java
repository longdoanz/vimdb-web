package com.viettel.imdb.rest.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.imdb.common.Filter;
import com.viettel.imdb.rest.RestErrorCode;
import com.viettel.imdb.rest.common.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author longdt20
 * @since 09:32 22/02/2019
 */

public class FilterModel {
    private String fieldName;

    private enum queryOption {
        $BT // between
    }

    private int start;
    private int end;

    public FilterModel() {
    }


    public RestErrorCode setData(String filter) {
        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readTree(filter);
        } catch (IOException e1) {
            return RestErrorCode.FILTER_NOT_IN_CORRECT_FORMAT;
        }

        AtomicInteger keyCount = new AtomicInteger();
        jsonNode.fieldNames().forEachRemaining(key -> {
            fieldName = key;
            keyCount.getAndIncrement();
        });
        if (keyCount.get() != 1) {
            return RestErrorCode.ONLY_SUPPORT_ONE_FIELD;
        }
        JsonNode value = jsonNode.get(fieldName);
        if (value.isInt()) {
            start = value.intValue();
            end = start + 1;
            return RestErrorCode.OK;

        } else if (value.isContainerNode()) {

            List<String> listKey = new ArrayList<>();
            value.fieldNames().forEachRemaining(listKey::add);

            if (listKey.size() != 1) {
                return RestErrorCode.FILTER_NOT_IN_CORRECT_FORMAT;
            }

            String subKey = listKey.get(0);
            return processContainerNode(value, subKey);

        } // if value not in [integer, json node] -> return error
        return RestErrorCode.FILTER_NOT_IN_CORRECT_FORMAT;
    }

    private RestErrorCode processContainerNode(JsonNode data, String key) {
        try {
            switch (queryOption.valueOf(key.toUpperCase())) {
                case $BT:
                    JsonNode value = data.get(key);
                    if (!value.isArray()) {
                        return RestErrorCode.FILTER_NOT_IN_CORRECT_FORMAT;
                    }

                    List<Integer> range = new ArrayList<>();
                    for (JsonNode node : value) {
                        range.add(node.intValue());
                    }
                    if (range.size() != 2) {
                        return RestErrorCode.FILTER_RANGE_INVALID;
                    } else {
                        start = range.get(0);
                        end = range.get(1);
                        return RestErrorCode.OK;
                    }
            }
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        }
        return RestErrorCode.FILTER_NOT_IN_CORRECT_FORMAT;
    }


    public Filter getFilterRange(String tableName) {
        return Filter.range(tableName, Utils.convertToIndexField(fieldName), start, end);
    }

}
