package com.viettel.imdb.rest.util;

import com.viettel.imdb.IMDBClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * IMDBClientPool is a pool of currently connected client
 *
 * @author quannh22
 * @since 03/10/2019
 */
@Service
public class IMDBClientPool {
    public static final int MAX_ACTIVE_CLIENTS = 5;
    public static final int IDLE_TIMEOUT_IN_MS = 15 * 60 * 1000;
    private Map<Map.Entry<String, byte[]>, String> userPassToTokenMap;
    private Map<String, IMDBClient> tokenToClientMap;
    // todo a timer to close an idle client after IDLE_TIMEOUT_IN_MS (ms)

    // todo configured
    @Value("${host}")
    private String host;
    @Value("${security.enabled}")
    private boolean securityEnabled;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;

    @Autowired
    IMDBClient client;

    /**
     * Create a default pool of empty username, password, todo OR WITH THE DEFAULT LOGIN USER (admin)?
     */
    public IMDBClientPool() {
        userPassToTokenMap = new ConcurrentHashMap<>();
        tokenToClientMap = new ConcurrentHashMap<>();
    }

    /**
     * When user login correctly with a username and a password, return a pair of token and IMDBClient
     * @param username input username
     * @param password input password
     * @return pair token and IMDBClient
     */
    public Map.Entry<String, IMDBClient> connect(String username, String password) {
        return null;
    }

    public IMDBClient getClient(String token) {
        return null;
    }

}
