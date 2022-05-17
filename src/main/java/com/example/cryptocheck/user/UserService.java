package com.example.cryptocheck.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.getById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow();
    }
}
