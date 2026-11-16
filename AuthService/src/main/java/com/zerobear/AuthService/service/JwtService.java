package com.zerobear.AuthService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final SecretKey secret;
    private final long expirationTime;

    @Autowired
    public JwtService(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expire-time}") long expirationTime) {

        this.secret = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );

        this.expirationTime = expirationTime;
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isExpired(String token){
        return  extractExpiration(token).before(new Date());
    }

    public  boolean validateToken(String token, UserDetails userDetails){
        final  String userName= extractUserName(token);
        return  (userName.equals(userDetails.getUsername()) && !isExpired(token));
    }

    public  String createToken(Map<String,Object> claims, String username){
        return  Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expirationTime))
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
    }
    public  String createToken( String username){
        return  Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expirationTime))
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
    }

    private  Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload();
    }
}
