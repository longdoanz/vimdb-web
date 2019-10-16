package com.viettel.imdb.rest.service;

import com.viettel.imdb.ErrorCode;
import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.JavaClient;
import com.viettel.imdb.common.ClientConfig;
import com.viettel.imdb.common.ClientException;
import com.viettel.imdb.common.Pair;
import com.viettel.imdb.rest.mock.client.ClientSimulator;
import com.viettel.imdb.rest.mock.server.ClusterSimulator;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import javax.xml.stream.events.EntityReference;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author quannh22
 * @since 03/10/2019
 */
@Service
public class AuthServiceImpl implements AuthService {
    // todo if SECURITY IS OFF :-?
    public static final int MAX_ACTIVE_CLIENTS = 20;
    public static final int IDLE_TIMEOUT_IN_MS = 15 * 60 * 1000;
    public static final Duration TIMEOUT = Duration.ofMillis(20);

    private Map<Pair<String, byte[]>, String> userPassToTokenMap;
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
    private final IMDBClient client;

    @Autowired
    public AuthServiceImpl(IMDBClient client) {
        this.client = client;

        userPassToTokenMap = new ConcurrentHashMap<>();
        tokenToClientMap = new ConcurrentHashMap<>();
    }

    @Override
    public DeferredResult<ResponseEntity<?>> login(String username, String password) {
        Logger.error("login({}, {})", username, password);
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        Pair<IMDBClient, String> existedClientAndToken = getClient(username, password);
        if(existedClientAndToken != null) {
            Map<String, Object> body = new HashMap<>();
            body.put("token", existedClientAndToken.getSecond());
            returnValue.setResult(new ResponseEntity<>(body, HttpStatus.OK));
            return returnValue;
        }
        if(!isToCreateNewClient()) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", "Cannot connect a new client. Contact admin for more detail");
            returnValue.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
            return returnValue;
        }
        Pair<IMDBClient, String> newClientAndToken = createNewClientWithToken(username,password);
        if(newClientAndToken == null) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", "Cannot create a new client. Contact admin for more detail");
            returnValue.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
            return returnValue;
        }
        Map<String, Object> body = new HashMap<>();
        body.put("token", newClientAndToken.getSecond());
        returnValue.setResult(new ResponseEntity<>(body, HttpStatus.OK));
        return returnValue;
    }

    /**
     *
     * Get a current client with input username and password. Return null if not exist
     *
     * @param username input username
     * @param password input password
     * @return the existed client, or null
     */
    private Pair<IMDBClient, String> getClient(String username, String password) {
        Pair<String, byte[]> pairUserPass = new Pair<>(username, password.getBytes());
        if(!userPassToTokenMap.containsKey(pairUserPass))
            return null;
        String token = userPassToTokenMap.get(pairUserPass);
        IMDBClient client = tokenToClientMap.get(token);
        return new Pair<>(client, token);
    }

    private boolean isToCreateNewClient() {
        Logger.error("isToCreateNewClient: userPassToTokenMapSize: {}", userPassToTokenMap.size());
        return userPassToTokenMap.size() < MAX_ACTIVE_CLIENTS;
    }

    private Pair<IMDBClient, String> createNewClientWithToken(String username, String password) {
        String newToken = username + "-" + password; // todo just temporary fake here
        try {
            IMDBClient client = new ClientSimulator(new ClusterSimulator());

//            IMDBClient client = new JavaClient(host, new ClientConfig(securityEnabled, username,password));
            userPassToTokenMap.put(new Pair<>(username, password.getBytes()), newToken);
            tokenToClientMap.put(newToken, client);
            return new Pair<>(client, newToken);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }





}
