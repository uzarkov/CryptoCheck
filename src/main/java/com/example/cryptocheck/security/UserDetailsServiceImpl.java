package com.example.cryptocheck.security;

import com.example.cryptocheck.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authService.getUserByEmail(email).asUserDetails();
    }
}
