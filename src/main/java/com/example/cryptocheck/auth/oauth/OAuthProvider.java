package com.example.cryptocheck.auth.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum OAuthProvider {
    NONE("none", "basic"),
    GITHUB("github", "GitHub");

    private final String registrationId;
    private final String friendlyName;

    public static boolean contains(String registrationId) {
        return Arrays.stream(OAuthProvider.values())
                .anyMatch(provider -> provider.getRegistrationId().equals(registrationId));
    }

    public static OAuthProvider forRegistrationId(String registrationId) {
        return Arrays.stream(OAuthProvider.values())
                .filter(provider -> provider.getRegistrationId().equals(registrationId))
                .findFirst()
                .orElse(NONE);
    }
}
