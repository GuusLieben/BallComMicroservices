package com.ball.auth.jwt;

import com.ball.auth.model.User;
import com.ball.auth.model.UserRole;
import com.ball.auth.model.jwt.UserJwtModel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    private static final String GUID = "guid";
    private static final String EMAIL = "email";
    private static final String ROLE = "role";

    public static final List<String> SKIP_KEYS = Arrays.asList(GUID, EMAIL, ROLE, "sub", "exp", "iat");

    @Value("${jwt.secret}")
    private String secret;

    public UserJwtModel getUserPayload(String token) {
        Claims claims = this.getAllClaimsFromToken(token);
        UserJwtModel model = new UserJwtModel();
        model.setGuid(UUID.fromString(String.valueOf(claims.get(GUID))));
        model.setEmail(String.valueOf(claims.get(EMAIL)));
        model.setRole(UserRole.valueOf(String.valueOf(claims.get(ROLE)).toUpperCase()));
        model.setMeta(new HashMap<>());

        for (Entry<String, Object> claim : claims.entrySet()) {
            String key = claim.getKey();
            if (SKIP_KEYS.contains(key)) continue;
            else {
                model.getMeta().put(key, String.valueOf(claim.getValue()));
            }
        }
        return model;
    }

    public Map<String, Object> getRawPayload(String token) {
        return this.getAllClaimsFromToken(token);
    }

    public Date getExpirationDateFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(GUID, user.getGuid());
        claims.put(EMAIL, user.getEmail());
        claims.put(ROLE, user.getRole());

        user.getMeta().forEach((k, v) -> {
            if (claims.containsKey(k)) throw new RuntimeException("Illegal duplicate key: " + k);
            else claims.put(k, v);
        });

        return this.doGenerateToken(claims, user.getEmail());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, this.secret).compact();
    }

    public Boolean validateToken(String token, UserJwtModel model) {
        final UserJwtModel payload = this.getUserPayload(token);
        return (model.equals(payload) && !this.isTokenExpired(token));
    }
}
