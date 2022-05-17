package com.example.cryptocheck.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.cryptocheck.security.JwtTokenFilter;
import com.example.cryptocheck.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final String jwtSigningSecret;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthController(@Value("${jwt.signing-secret}") String jwtSigningSecret,
                          AuthenticationManager authenticationManager,
                          UserRepository userRepository) {
        this.jwtSigningSecret = jwtSigningSecret;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticatedUserMetadata> login(@RequestBody LoginRequestBody loginRequestBody) {
        try {
            var email = loginRequestBody.email();
            var password = loginRequestBody.password();
            var authToken = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authToken);

            var user = userRepository.getUserByEmail(email).orElseThrow();
            var jwt = JWT.create()
                    .withClaim(JwtTokenFilter.EMAIL_CLAIM, user.getEmail())
                    .sign(Algorithm.HMAC256(jwtSigningSecret));

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwt)
                    .body(AuthenticatedUserMetadata.from(user));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
