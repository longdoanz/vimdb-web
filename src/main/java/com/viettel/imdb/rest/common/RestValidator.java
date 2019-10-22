package com.viettel.imdb.rest.common;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.rest.exception.ExceptionType;
import org.pmw.tinylog.Logger;

/**
 * @author quannh22
 * @since 26/08/2019
 */
public class RestValidator {
    public static void validateNamespace(String namespace) {
        if(!namespace.equals("namespace")) {
            Logger.error("namespace must be \"namespace\" by now");
            throw new ExceptionType.VIMDBRestClientError("namespace must be \"namespace\" by now");
        }
    }
}
