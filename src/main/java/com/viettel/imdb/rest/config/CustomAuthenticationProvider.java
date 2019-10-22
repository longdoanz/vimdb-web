package com.viettel.imdb.rest.config;

import com.viettel.imdb.rest.common.Result;
import com.viettel.imdb.rest.model.CustomUserDetails;
import com.viettel.imdb.rest.model.Privilege;
import com.viettel.imdb.rest.model.PrivilegeType;
import com.viettel.imdb.rest.model.User;
import com.viettel.imdb.rest.service.AuthService;
import com.viettel.imdb.rest.service.AuthServiceImpl;
import com.viettel.imdb.rest.service.UserService;
import io.trane.future.CheckedFutureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

//    @Value("${super.admin}")
//    private String superAdmin;
//
//    @Value("${super.admin.pass}")
//    private String superAdminPassword;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthServiceImpl authService;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {


        CustomUserDetails user = (CustomUserDetails) authService.loadUserByUsername(authentication.getName());

        //xac thuc
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UsernamePasswordAuthenticationToken result = null;
        if(user.getUser().getUsername().equals(username)&&user.getUser().getPassword().equals(password)){
            result = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            //result.setDetails(user);
        }
        return result;
        /*


        if (superAdmin.equals(username) && superAdminPassword.equals(password)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            Privilege privilege = new Privilege();
            privilege.setType(PrivilegeType.SUPER_ADMIN);
            user.setPrivileges(Arrays.asList(privilege));
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            token.setDetails(user);
            return token;
        }

        User user = null;
        try {
            Result result = userService.getUser(username).get(Duration.ofDays(1));
            if(result.getData()!=null) {
                user = (User)result.getData();
            }
        } catch (CheckedFutureException e) {
            e.printStackTrace();
        }

        if (user!=null && passwordEncoder.matches(password, user.getPassword())) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            token.setDetails(user);
            return token;
        } else {
            return null;
        }*/
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
