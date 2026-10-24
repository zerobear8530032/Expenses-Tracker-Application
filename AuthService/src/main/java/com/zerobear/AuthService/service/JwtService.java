package com.zerobear.AuthService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    public static final SecretKey SECRET= Keys.hmacShaKeyFor( "YyYohW8XZqpzFxnk57muAeGtqqKkdqdSvToGBm3ESFt".getBytes());

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
                .expiration(new Date(System.currentTimeMillis()*1000*60*60*1))
                .signWith(SECRET, SignatureAlgorithm.HS256)
                .compact();
    }
    public  String createToken( String username){
        return  Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()*1000*60*60*1))
                .signWith(SECRET, SignatureAlgorithm.HS256)
                .compact();
    }

    private  Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(SECRET).build().parseSignedClaims(token).getPayload();
    }
}
