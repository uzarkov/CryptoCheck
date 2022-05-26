package com.example.cryptocheck.auth.oauth

import com.example.cryptocheck.auth.JwtUtils
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class OAuth2AuthenticationSuccessHandlerSpec extends Specification {
    def jwtUtils = Mock(JwtUtils) {
        createJwtFor(_ as String) >> "jwt"
    }
    def request = Mock(HttpServletRequest)
    def response = Mock(HttpServletResponse) {
        encodeRedirectURL(_ as String) >> { String url -> url }
    }

    def "onAuthenticationSuccess() SHOULD redirect to some url with access token as query param"() {
        given:
        def redirectUrl = "http://localhost:3000/redirect"
        def handler = new OAuth2AuthenticationSuccessHandler(redirectUrl, jwtUtils)
        def authentication = Mock(OAuth2AuthenticationToken) {
            getPrincipal() >> Mock(OAuth2User) {
                getAttribute("email") >> ""
            }
        }

        when:
        handler.onAuthenticationSuccess(request, response, authentication)

        then:
        response.sendRedirect(_ as String) >> { String location -> assert location.contains("?access_token=jwt") }
    }
}
