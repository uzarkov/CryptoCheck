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
        var name = oauth2User.<String>getAttribute("name");
        var email = oauth2User.<String>getAttribute("email");

        var user = appUserRepository.getUserByEmail(email);
        if (user.isPresent()) {
            var existingUser = user.get();
            if (existingUser.getAuthProvider() != authProvider) {
                var msg = AuthException.invalidProvider(existingUser.getAuthProvider()).getMessage();
                throw new OAuth2AuthenticationException(msg);
            }
            updateExistingUser(existingUser, name);
        } else {
            registerNewUser(name, email, authProvider);
        }

        return oauth2User;
    }

    private void registerNewUser(String name, String email, OAuthProvider authProvider) {
        var newUser = new AppUser();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        newUser.setAuthProvider(authProvider);
        appUserRepository.save(newUser);
    }

    private void updateExistingUser(AppUser existingUser, String name) {
        existingUser.setName(name);
        appUserRepository.save(existingUser);
    }
}
