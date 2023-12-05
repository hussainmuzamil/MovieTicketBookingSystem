package com.example.movieticketingsystem.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTService {

    @Value("${application.security.jwt.secret-key}")
    private String SECRET;
    @Value("${application.security.jwt.expiration}")
    private Long expiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshExpiration;
    public String generateToken(UserDetails user){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,user);
    }

    public String createToken(Map<String, Object> claims, UserDetails user) {
        return buildToken(claims,user,expiration);
    }
    public String buildToken(Map<String, Object> extraClaims, UserDetails user, Long expiration){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public Key getSignInKey() {
        byte[] key = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(key);
    }
    public String extractUsername(String jwt){
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String jwt, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaim(jwt);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaim(String jwt) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwt).getBody();
    }
    public boolean isTokenValid(String jwt, UserDetails userDetails){
        final String username = extractUsername(jwt);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
    }

    public boolean isTokenExpired(String jwt) {
        return extractClaim(jwt,Claims::getExpiration).before(new Date());
    }

    public String regenerateToken(HashMap<String, Object> extraClaims, UserDetails user) {
        return buildToken(extraClaims,user,refreshExpiration);
    }
}
