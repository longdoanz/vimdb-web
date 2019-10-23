package com.viettel.imdb.rest.common;

import com.viettel.imdb.rest.exception.ExceptionType;

/**
 * @author quannh22
 * @since 26/08/2019
 */
public class RestValidator {
    public static void validateNamespace(String namespace) {
        if(!namespace.equals("namespace"))
            throw new ExceptionType.VIMDBRestClientError("namespace must be \"namespace\" by now");
    }
}
