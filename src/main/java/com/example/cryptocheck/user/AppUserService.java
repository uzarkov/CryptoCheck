package com.example.cryptocheck.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUser getUserById(Long id) {
        return appUserRepository.getById(id);
    }

    public AppUser getUserByEmail(String email) {
        return appUserRepository.getUserByEmail(email).orElseThrow();
    }
}
