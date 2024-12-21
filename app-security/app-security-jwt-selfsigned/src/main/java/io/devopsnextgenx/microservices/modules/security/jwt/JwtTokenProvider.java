package io.devopsnextgenx.microservices.modules.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.devopsnextgenx.microservices.modules.models.Role;
import io.devopsnextgenx.microservices.modules.principal.AppxUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.JwtException;
import java.util.Collection;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
public class JwtTokenProvider {
    @Value("${app.modules.security.jwt.secret}")
    private String secretKey;

    @Value("${app.modules.security.jwt.expiration}")
    private long validityInMilliseconds;

    public String createToken(Authentication authentication) {
        AppxUserPrincipal userDetails = (AppxUserPrincipal) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        Claims claims = Jwts.claims()
        .subject(userDetails.getUsername())
        .add("roles", roles)
        .add("email", userDetails.getEmail())
        .build();

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .claims(claims)
            .issuedAt(now)
            .expiration(validity)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey).build()
            .parseClaimsJws(token)
            .getBody();

        UserDetails userDetails = User.builder()
            .username(claims.getSubject())
            .password("")
            .authorities((Collection<? extends GrantedAuthority>) claims.get("roles"))
            .build();
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}