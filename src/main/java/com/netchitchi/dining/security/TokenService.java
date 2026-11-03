package com.netchitchi.dining.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.netchitchi.dining.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;
    private static final String ISSUER = "dining-review";

    public String generateToken(User user){

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(user.getName())
                .withClaim("id", user.getId())
                .withClaim("role", user.getRole().name())
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .sign(Algorithm.HMAC256(secret));
    }

    public Optional<String> validateToken(String token) {
        try {
            String name = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
            return Optional.ofNullable(name);
        } catch (JWTVerificationException e) {
            return Optional.empty();   // not an error: the request just isn't authenticated
        }
    }


}
