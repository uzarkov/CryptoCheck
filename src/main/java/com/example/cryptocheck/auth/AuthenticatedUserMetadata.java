package com.example.cryptocheck.auth;

import com.example.cryptocheck.user.User;

public record AuthenticatedUserMetadata(Long id, String name, String email) {
    public static AuthenticatedUserMetadata from(User appUser) {
        return new AuthenticatedUserMetadata(
                appUser.getId(),
                appUser.getName(),
                appUser.getEmail()
        );
    }
}
