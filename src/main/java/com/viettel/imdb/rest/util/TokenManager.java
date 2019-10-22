package com.viettel.imdb.rest.util;

import com.viettel.imdb.rest.model.CustomUserDetails;
import io.jsonwebtoken.*;
import org.pmw.tinylog.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author quannh22
 * @since 03/10/2019
 */
@Component
public class TokenManager {

    private final String JWT_SECRET = "lodaaaaaa";

    public static final int IDLE_TIMEOUT_IN_MS = 15 * 60 * 1000;

    // Tạo ra jwt từ thông tin user
    public String generateToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + IDLE_TIMEOUT_IN_MS);
        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(userDetails.getUser().getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    // Lấy thông tin user từ jwt
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            Logger.error("Invalid JWT token");
            SecurityContextHolder.clearContext();
        } catch (ExpiredJwtException ex) {
            Logger.error("Expired JWT token");
            SecurityContextHolder.clearContext();
        } catch (UnsupportedJwtException ex) {
            Logger.error("Unsupported JWT token");
            SecurityContextHolder.clearContext();
        } catch (IllegalArgumentException ex) {
            Logger.error("JWT claims string is empty.");
            SecurityContextHolder.clearContext();
        }
        return false;
    }
}
