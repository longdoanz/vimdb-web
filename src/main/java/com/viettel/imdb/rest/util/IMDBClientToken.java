package com.viettel.imdb.rest.util;

import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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

    private static Map<String, IMDBClient> tokenToClientMap;
    private static Map<Pair<String, String>, String> userPassToTokenMap;
    private static IMDBClientToken clientToken;
    private static Timer timer;

    //@Autowired
    TokenManager tokenManager = new TokenManager();
    public IMDBClientToken() {
        tokenToClientMap = new ConcurrentHashMap<>();
        userPassToTokenMap = new ConcurrentHashMap<>();
        timer = new Timer();
        timer.schedule(new RemoveExpiredToken(), 0,1 * 1000);
    }

    class RemoveExpiredToken extends TimerTask {
        @Override
        public void run(){
            //System.out.println("RemoveExpiredToken");
            for(String key:tokenToClientMap.keySet()){
                //System.out.println(key  + tokenManager.validateToken(key));
                if(!tokenManager.validateToken(key)){
                    //System.out.println("2.Remove Expired Token");
                    tokenToClientMap.remove(key);
                }
            }
            for(Pair<String, String> userPass:userPassToTokenMap.keySet()){
                if(!tokenManager.validateToken(userPassToTokenMap.get(userPass))){
                    userPassToTokenMap.remove(userPass);
                }
            }
        }
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
    public static void putClient(String token, IMDBClient client) {
        tokenToClientMap.put(token, client);
    }
    public static String getToken(Pair<String, String> userPass) {
        return userPassToTokenMap.get(userPass);
    }
    public static void putUserPassToTokenMap(Pair<String, String> userPass, String token) {
        userPassToTokenMap.put(userPass, token);
    }
    public static int getUserPassToTokenMapSize() {
        return userPassToTokenMap.size();
    }
    public static boolean userPassToTokenMapContainsKey(Pair<String, String> userPass) {
        return userPassToTokenMap.containsKey(userPass);
    }

}
