package com.viettel.imdb.rest.service;

import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.Pair;
import com.viettel.imdb.core.security.User;
import com.viettel.imdb.rest.config.CustomAuthenticationProvider;
import com.viettel.imdb.rest.mock.client.ClientSimulator;
import com.viettel.imdb.rest.mock.server.ClusterSimulator;
import com.viettel.imdb.rest.model.AuthenRespone;
import com.viettel.imdb.rest.model.CustomUserDetails;
import com.viettel.imdb.rest.util.IMDBClientToken;
import com.viettel.imdb.rest.util.TokenManager;
import io.trane.future.Future;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author quannh22
 * @since 03/10/2019
 */
@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {
    // todo if SECURITY IS OFF :-?
    public static final int MAX_ACTIVE_CLIENTS = 20;
    public static final int IDLE_TIMEOUT_IN_MS = 15 * 60 * 1000;
    public static final Duration TIMEOUT = Duration.ofMillis(20);

    private Map<String, String> tokenToUserMap;
    //private Map<Pair<String, byte[]>, String> userPassToTokenMap;
    //private Map<Pair<String, String>, String> userPassToTokenMap;
    private Map<String, IMDBClient> tokenToClientMap;
    private IMDBClientToken imdbClientToken;
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
    CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    TokenManager tokenManager;

    @Autowired
    public AuthServiceImpl(IMDBClient client) {
        this.client = client;

//        userPassToTokenMap = new ConcurrentHashMap<>();
//        tokenToClientMap = new ConcurrentHashMap<>();
        imdbClientToken = new IMDBClientToken();
    }

    @Override
    public DeferredResult<ResponseEntity<?>> login(String username, String password) {
        Logger.info("login({}, {})", username, password);
        UserDetails user = loadUserByUsername("admin");
        Logger.info("user ({} {})", user.getUsername(), user.getPassword());

        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();
        Pair<IMDBClient, String> existedClientAndToken = getClient(username, password);
        if(existedClientAndToken != null) {
            //Map<String, Object> body = new HashMap<>();
            AuthenRespone authenRespone = new AuthenRespone(existedClientAndToken.getSecond());
            //body.put("token", existedClientAndToken.getSecond());
            returnValue.setResult(new ResponseEntity<>(authenRespone, HttpStatus.OK));
            Logger.info("existedClientAndToken, return token");
            return returnValue;
        }

        if(!isToCreateNewClient()) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", "Cannot connect a new client. Contact admin for more detail");
            returnValue.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
            return returnValue;
        }

        //xac thuc
        Authentication authentication = customAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (authentication == null){
            Map<String, Object> body = new HashMap<>();
            body.put("error", "Unauthorized");
            returnValue.setResult(new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED));
            return returnValue;
        }
        String token = tokenManager.generateToken((CustomUserDetails) authentication.getPrincipal());
        Pair<IMDBClient, String> newClientAndToken = createNewClientWithToken(username,password,token);
        if(newClientAndToken == null) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", "Cannot create a new client. Contact admin for more detail");
            returnValue.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
            return returnValue;
        }
        Logger.info("create token({}, {})", username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Tra token cho user
        AuthenRespone authenRespone = new AuthenRespone(token);
        returnValue.setResult(new ResponseEntity<>(authenRespone, HttpStatus.OK));
        return returnValue;

        /*
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
        return returnValue;*/
    }

    @Override
    public DeferredResult<ResponseEntity<?>> createAuthenticationToken(String username, String password) {
        DeferredResult<ResponseEntity<?>> returnValue = new DeferredResult<>();

        //validate user name

        //gen token
        Pair<IMDBClient, String> newClientAndToken = createNewClientWithToken(username,password, "token");
        if(newClientAndToken == null) {
            Map<String, Object> body = new HashMap<>();
            body.put("error", "Cannot create a new client. Contact admin for more detail");
            returnValue.setResult(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
            return returnValue;
        }
        AuthenRespone respone = new AuthenRespone(newClientAndToken.getSecond());
//        Map<String, Object> body = new HashMap<>();
//        body.put("token", newClientAndToken.getSecond());
        returnValue.setResult(new ResponseEntity<>(respone, HttpStatus.OK));
        return returnValue;

    }

    @Override
    public String getUsernameFromeToken(String token) {
        return null;
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
        //Pair<String, byte[]> pairUserPass = new Pair<>(username, password.getBytes());
        Pair<String, String> pairUserPass = new Pair<>(username, password);
        String token2 = IMDBClientToken.getToken(pairUserPass);
        Logger.info("get token client {} {}",password.getBytes(), password.getBytes());
        byte[] a = password.getBytes();
        System.out.println(a);
        for (byte b : a){
            System.out.print(b);
        }
        System.out.println("leng"+ a.toString());
        if(!IMDBClientToken.userPassToTokenMapContainsKey(pairUserPass))
            return null;
        String token = IMDBClientToken.getToken(pairUserPass);
        IMDBClient client = IMDBClientToken.getClient(token);
        return new Pair<>(client, token);
    }

    private boolean isToCreateNewClient() {
        Logger.info("isToCreateNewClient: userPassToTokenMapSize: {}", IMDBClientToken.getUserPassToTokenMapSize());
        return IMDBClientToken.getUserPassToTokenMapSize() < MAX_ACTIVE_CLIENTS;
    }

    private Pair<IMDBClient, String> createNewClientWithToken(String username, String password, String newToken) {
        try {
            Logger.info("Create client with token");
            IMDBClient client = new ClientSimulator(new ClusterSimulator());

//            IMDBClient client = new JavaClient(host, new ClientConfig(securityEnabled, username,password));
            //userPassToTokenMap.put(new Pair<>(username, password.getBytes()), newToken);
            IMDBClientToken.putUserPassToTokenMap(new Pair<>(username, password), newToken);
            IMDBClientToken.putClient(newToken, client);
            //tokenToUserMap.put(newToken, username);
            return new Pair<>(client, newToken);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    /*

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + IDLE_TIMEOUT_IN_MS*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }
    public String getUsernameFromToken(String token) {
        //String newToken = username + "-" + password; // todo just temporary fake here
        return getClaimFromToken(token, Claims::getSubject);
//        String username = tokenToUserMap.get(token);
//        return username;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public Boolean validateToken(String token) {
        //String newToken = username + "-" + password; // todo just temporary fake here
        String username = tokenToUserMap.get(token);
        return true;
        //return username;
    }*/

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Future<User> getUserFuture = client.readUser(s);
        CustomUserDetails userResult = new CustomUserDetails();
        getUserFuture
                .onSuccess(user ->{
                    if (user == null) throw new UsernameNotFoundException(username);
                    else userResult.setUser(user);
                        })
                .onFailure(throwable ->{
                    userResult.setUser(null);
                    //throw new UsernameNotFoundException(username);
                    throwable.printStackTrace();
                });
        if (userResult.getUser() == null){
            throw new UsernameNotFoundException(username);
        }
        return userResult;
//        Future<Result> resultFuture = getUserFuture
//                .map(user -> new Result(HttpStatus.OK, user))
//                .rescue(throwable -> throwableToHttpStatus(throwable));
    }
}
