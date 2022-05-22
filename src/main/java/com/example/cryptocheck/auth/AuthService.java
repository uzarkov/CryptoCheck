package com.example.cryptocheck.auth;

import com.example.cryptocheck.user.AppUser;
import com.example.cryptocheck.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AppUserRepository userRepository;

    public AppUser getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow();
    }
}
