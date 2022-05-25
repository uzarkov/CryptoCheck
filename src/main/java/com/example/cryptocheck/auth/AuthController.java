package com.example.cryptocheck.auth;

import com.example.cryptocheck.auth.oauth.OAuthProvider;
import com.example.cryptocheck.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository userRepository;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<AuthenticatedUserMetadata> login(@RequestBody LoginRequestBody loginRequestBody) {
        try {
            var email = loginRequestBody.email();
            var user = userRepository.getUserByEmail(email).orElseThrow(AuthException::invalidCredentials);
            if (user.getAuthProvider() != OAuthProvider.NONE) {
                throw AuthException.invalidCredentials();
            }

            var password = loginRequestBody.password();
            var authToken = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authToken);

            return ResponseEntity.ok()
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                    .header(HttpHeaders.AUTHORIZATION, jwtUtils.createJwtFor(user.getEmail()))
                    .body(AuthenticatedUserMetadata.from(user));
        } catch (BadCredentialsException ex) {
            throw AuthException.invalidCredentials();
        }
    }
}
