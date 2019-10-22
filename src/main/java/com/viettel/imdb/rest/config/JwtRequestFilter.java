package com.viettel.imdb.rest.config;

import com.viettel.imdb.rest.service.AuthServiceImpl;
import com.viettel.imdb.rest.util.TokenManager;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    AuthServiceImpl service;

    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        System.out.println("Authorization:"+requestTokenHeader+"cookie: "+httpServletRequest.getHeader("Cookie")+ "Accept:"+httpServletRequest.getHeader("Accept"));

        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
            jwtToken = getJwtFromRequest(httpServletRequest);

            if(tokenManager.validateToken(jwtToken)&&StringUtils.hasText(jwtToken)){
                //Lay username
                Logger.error("Lay user");
                username = tokenManager.getUsernameFromJWT(jwtToken);

                UserDetails userDetails = service.loadUserByUsername(username);
                if(userDetails != null) {
                    // set thông tin cho Seturity Context
                    Logger.info("set context");
                    UsernamePasswordAuthenticationToken
                            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }//else{
//                filterChain.doFilter(httpServletRequest, httpServletResponse);
//                return;
//            }


        Logger.error("done filter");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

}
