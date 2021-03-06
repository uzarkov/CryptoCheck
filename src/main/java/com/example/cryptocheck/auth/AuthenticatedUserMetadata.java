package com.example.cryptocheck.auth;

import com.example.cryptocheck.user.AppUser;

public record AuthenticatedUserMetadata(Long id, String name, String email, String avatarUrl) {
    public static AuthenticatedUserMetadata from(AppUser appUser) {
        return new AuthenticatedUserMetadata(
                appUser.getId(),
                appUser.getName(),
                appUser.getEmail(),
                appUser.getAvatarUrl()
        );
    }
}
