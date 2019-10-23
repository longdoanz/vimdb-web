package com.viettel.imdb.rest.common;

import com.viettel.imdb.ErrorCode;
import org.pmw.tinylog.Logger;

/**
 * @author quannh22
 * @since 26/08/2019
 */
public class RestValidator {
    public static ErrorCode validateNamespace(String namespace) {
        if(namespace.equals("namespace"))
            return ErrorCode.NO_ERROR;
        Logger.error("namespace must be \"namespace\" by now");
        return ErrorCode.INTERNAL_ERROR;
    }
}
