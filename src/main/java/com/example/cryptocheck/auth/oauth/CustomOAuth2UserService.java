package com.example.cryptocheck.auth.oauth;

import com.example.cryptocheck.auth.AuthException;
import com.example.cryptocheck.user.AppUser;
import com.example.cryptocheck.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final DefaultOAuth2UserService defaultOAuth2UserService;

    @Autowired
    public CustomOAuth2UserService(AppUserRepository appUserRepository,
                                   PasswordEncoder passwordEncoder) {
        this(appUserRepository, passwordEncoder, new DefaultOAuth2UserService());
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        var registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (!OAuthProvider.contains(registrationId)) {
            throw new OAuth2AuthenticationException("OAuth provider <%s> is not supported.".formatted(registrationId));
        }

        var authProvider = OAuthProvider.forRegistrationId(registrationId);
        var oauth2User = defaultOAuth2UserService.loadUser(userRequest);
        var name = getAttribute(oauth2User, "name");
        var email = getAttribute(oauth2User, "email");
        var avatarUrl = getAttribute(oauth2User, "avatar_url");

        var user = appUserRepository.getUserByEmail(email);
        if (user.isPresent()) {
            var existingUser = user.get();
            if (existingUser.getAuthProvider() != authProvider) {
                var msg = AuthException.invalidProvider(existingUser.getAuthProvider()).getMessage();
                throw new OAuth2AuthenticationException(msg);
            }
            updateExistingUser(existingUser, name, avatarUrl);
        } else {
            registerNewUser(name, email, authProvider, avatarUrl);
        }

        return oauth2User;
    }

    private String getAttribute(OAuth2User oAuth2User, String attibuteName) {
        var attribute = oAuth2User.<String>getAttribute(attibuteName);
        return attribute == null ? "" : attribute;
    }

    private void registerNewUser(String name, String email, OAuthProvider authProvider, String avatarUrl) {
        var newUser = new AppUser();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        newUser.setAuthProvider(authProvider);

        if (!avatarUrl.isBlank()) {
            newUser.setAvatarUrl(avatarUrl);
        }

        appUserRepository.save(newUser);
    }

    private void updateExistingUser(AppUser existingUser, String name, String avatarUrl) {
        existingUser.setName(name);

        if (!avatarUrl.isBlank()) {
            existingUser.setAvatarUrl(avatarUrl);
        }

        appUserRepository.save(existingUser);
    }
}
