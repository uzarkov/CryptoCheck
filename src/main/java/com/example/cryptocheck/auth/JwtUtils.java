package com.example.cryptocheck.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtUtils {
    public static final String EMAIL_CLAIM = "email";

    private final String jwtSigningSecret;

    public JwtUtils(@Value("${jwt.signing-secret}") String jwtSigningSecret) {
        this.jwtSigningSecret = jwtSigningSecret;
    }

    public String createJwtFor(String email) {
        return JWT.create()
                .withClaim(JwtUtils.EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC256(jwtSigningSecret));
    }

    public Optional<DecodedJWT> decodeJwt(String jwt) {
        try {
            var verifier = JWT.require(Algorithm.HMAC256(jwtSigningSecret)).build();
            return Optional.of(verifier.verify(jwt));
        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }
}
