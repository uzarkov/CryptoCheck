package com.example.cryptocheck.user;

import com.example.cryptocheck.auth.AuthenticatedUserMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping(path = "/me")
    public ResponseEntity<AuthenticatedUserMetadata> getCurrentUserMetadata(Principal principal) {
        var email = principal.getName();
        return ResponseEntity.ok(AuthenticatedUserMetadata.from(appUserService.getUserByEmail(email)));
    }
}
