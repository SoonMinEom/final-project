package com.soonmin.final_project.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtil {

    private static Claims openToken(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public static boolean isExpired(String token, String secretKey) {
        Date expiredDate = openToken(token, secretKey).getExpiration();
        return expiredDate.before(new Date());
    }

    public static String getUserName(String token, String secretKey) {
        return openToken(token, secretKey).get("userName").toString();
    }

    public static String createToken(String userName, String secretKey, long expireTime) {
        log.info("username : {}", userName);
        log.info("scretKey : {}", secretKey);
        Claims claims = Jwts.claims();
        claims.put("userName", userName);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expireTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
