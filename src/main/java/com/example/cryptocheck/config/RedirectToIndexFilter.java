package com.example.cryptocheck.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class RedirectToIndexFilter extends GenericFilterBean {
    private final List<String> protectedEndpoints = new LinkedList<>();

    public RedirectToIndexFilter(Environment env) {
        protectedEndpoints.addAll(List.of("/api", "/oauth2/redirect", "/static"));

        var activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains("developer")) {
            protectedEndpoints.add("/h2-console");
        }
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();

        if (requestURI.equals("/") || protectedEndpoints.stream().anyMatch(requestURI::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        req.getRequestDispatcher("/").forward(request, response);
    }
}