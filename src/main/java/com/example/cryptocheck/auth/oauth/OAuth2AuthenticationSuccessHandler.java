package com.example.cryptocheck.auth.oauth;

import com.example.cryptocheck.auth.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final String successRedirectUrl;
    private final JwtUtils jwtUtils;

    public OAuth2AuthenticationSuccessHandler(@Value("${oauth2.success-redirect-url}") String successRedirectUrl,
                                              JwtUtils jwtUtils) {
        this.successRedirectUrl = successRedirectUrl;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        var isOAuth2Authentication = authentication instanceof OAuth2AuthenticationToken;
        if (!isOAuth2Authentication) {
            super.onAuthenticationSuccess(request, response, authentication);
        }

        var oauthUser = (OAuth2User) authentication.getPrincipal();
        var email = oauthUser.<String>getAttribute("email");

        getRedirectStrategy().sendRedirect(request, response, getRedirectUrlFor(email));
    }

    private String getRedirectUrlFor(String email) {
        var jwt = jwtUtils.createJwtFor(email);

        return UriComponentsBuilder.fromUriString(successRedirectUrl)
                .queryParam("access_token", jwt)
                .build()
                .toUriString();
    }
}
