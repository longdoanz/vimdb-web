package com.viettel.imdb.rest.util;

import com.viettel.imdb.IMDBClient;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author quannh22
 * @since 03/10/2019
 */
@Getter
@Setter
@ToString
public class IMDBClientToken {
//    private String token;
//    private String username;
//    private byte[] password;

    public static Map<String, IMDBClient> tokenToClientMap = new ConcurrentHashMap<>();
    private static IMDBClientToken clientToken;

    // private constructor to force use of
    // getInstance() to create Singleton object
    public IMDBClientToken() {
        tokenToClientMap = new ConcurrentHashMap<>();
    }

    public static IMDBClientToken getInstance()
    {
        if (clientToken==null)
            clientToken = new IMDBClientToken();
        return clientToken;
    }

    public static IMDBClient getClient(String token) {
        return tokenToClientMap.get(token);
    }

}
