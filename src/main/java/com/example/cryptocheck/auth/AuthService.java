package com.example.cryptocheck.auth;

import com.example.cryptocheck.user.User;
import com.example.cryptocheck.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow();
    }
}
