package com.fonctionpublique.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class DemandeurUtlil {
    private final SecretKey DEMANDE = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(DEMANDE).parseClaimsJws(token).getBody();
    }

    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token){
        return  extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){

        return  extractClaims(token, Claims::getExpiration);
    }


    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Boolean validationToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    ////    public Boolean TokenValidation(String token){
////        final String username = extractUsername(token);
////        return (username.equals(!isTokenExpired(token));
////    }
    @Transactional
    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 heure d'expiration
                .signWith(SignatureAlgorithm.HS256, DEMANDE)
                .compact();
    }
    //    public String generateToken(String username, String role){
//        Map<String,Object> claims = new HashMap<>();
//        claims.put("role",role);
//
//        return createToken(claims, username);
    public String generateToken(String username){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims, username);
    }
}
