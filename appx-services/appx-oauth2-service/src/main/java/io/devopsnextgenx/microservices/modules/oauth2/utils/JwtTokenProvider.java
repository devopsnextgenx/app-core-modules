package io.devopsnextgenx.microservices.modules.oauth2.utils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    @Value("${appx.modules.security.jwtSecret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:36000000}")
    private int jwtExpiration;

    public String createToken(OAuth2User oauth2User) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(Base64.getEncoder().encodeToString(jwtSecret.getBytes())));

        return Jwts.builder()
            .subject(oauth2User.getAttribute("login"))
            .issuedAt(now)
            .expiration(expiryDate)
            .claim("name", oauth2User.getAttribute("name"))
            .claim("email", oauth2User.getAttribute("email"))
            .signWith(key)
            .compact();
    }
}