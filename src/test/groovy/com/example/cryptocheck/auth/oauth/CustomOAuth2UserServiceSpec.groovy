package com.example.cryptocheck.auth.oauth

import com.example.cryptocheck.auth.AuthException
import com.example.cryptocheck.user.AppUser
import com.example.cryptocheck.user.AppUserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import spock.lang.Specification

class CustomOAuth2UserServiceSpec extends Specification {
    def appUserRepository = Mock(AppUserRepository)
    def passwordEncoder = Mock(PasswordEncoder) { encode(_ as String) >> { String str -> str } }
    def defaultOAuth2UserService = Mock(DefaultOAuth2UserService)
    def customOAuth2UserService = new CustomOAuth2UserService(appUserRepository, passwordEncoder, defaultOAuth2UserService)

    def "loadUser() WHEN client registration id is not supported SHOULD throw an exception"() {
        given:
        def userRequest = Mock(OAuth2UserRequest) {
            getClientRegistration() >> clientRegistrationWithRegistrationId("unknown")
        }

        when:
        customOAuth2UserService.loadUser(userRequest)

        then:
        def ex = thrown(OAuth2AuthenticationException)
        ex.getError().getErrorCode().contains("not supported")
    }

    def "loadUser() WHEN user is present but provider in request is different from user's initial provider SHOULD throw an exception"() {
        given:
        def existingUser = Mock(AppUser) { getAuthProvider() >> OAuthProvider.NONE }
        def userRequest = Mock(OAuth2UserRequest) {
            getClientRegistration() >> clientRegistrationWithRegistrationId(OAuthProvider.GITHUB.getRegistrationId())
        }

        defaultOAuth2UserService.loadUser(userRequest) >> Mock(OAuth2User) {
            getAttribute("name") >> "username"
            getAttribute("email") >> "user@test.com"
        }
        appUserRepository.getUserByEmail("user@test.com") >> Optional.of(existingUser)

        when:
        customOAuth2UserService.loadUser(userRequest)

        then:
        def ex = thrown(OAuth2AuthenticationException)
        ex.getError().getErrorCode() == AuthException.invalidProvider(existingUser.getAuthProvider()).getMessage()
    }

    def "loadUser() WHEN user is present and providers match SHOULD update existing user information"() {
        given:
        def existingUser = Mock(AppUser) { getAuthProvider() >> OAuthProvider.GITHUB }
        def userRequest = Mock(OAuth2UserRequest) {
            getClientRegistration() >> clientRegistrationWithRegistrationId(OAuthProvider.GITHUB.getRegistrationId())
        }

        defaultOAuth2UserService.loadUser(userRequest) >> Mock(OAuth2User) {
            getAttribute("name") >> "username"
            getAttribute("email") >> "user@test.com"
        }
        appUserRepository.getUserByEmail("user@test.com") >> Optional.of(existingUser)

        when:
        customOAuth2UserService.loadUser(userRequest)

        then:
        1 * existingUser.setName("username")
        1 * appUserRepository.save(existingUser)
    }

    def "loadUser() WHEN user does not exist SHOULD register new user"() {
        given:
        def userRequest = Mock(OAuth2UserRequest) {
            getClientRegistration() >> clientRegistrationWithRegistrationId(OAuthProvider.GITHUB.getRegistrationId())
        }

        defaultOAuth2UserService.loadUser(userRequest) >> Mock(OAuth2User) {
            getAttribute("name") >> "username"
            getAttribute("email") >> "user@test.com"
        }
        appUserRepository.getUserByEmail("user@test.com") >> Optional.empty()

        when:
        customOAuth2UserService.loadUser(userRequest)

        then:
        1 * appUserRepository.save(_ as AppUser) >> { AppUser newUser ->
            assert newUser.getName() == "username"
            assert newUser.getEmail() == "user@test.com"
            assert !newUser.getPassword().isBlank()
            assert newUser.getAuthProvider() == OAuthProvider.GITHUB
        }
    }

    private static ClientRegistration clientRegistrationWithRegistrationId(String registrationId) {
        return ClientRegistration
                .withRegistrationId(registrationId)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .clientId("client-id")
                .tokenUri("token-uri")
                .userInfoUri("user-info-uri")
                .userNameAttributeName("username-attribute-name")
                .build()
    }
}
