package com.example.cryptocheck.dev;

import com.example.cryptocheck.auth.oauth.OAuthProvider;
import com.example.cryptocheck.cryptocurrency.CryptocurrencyRepository;
import com.example.cryptocheck.user.AppUser;
import com.example.cryptocheck.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("developer")
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CryptocurrencyRepository cryptocurrencyRepository;

    @Override
    public void run(ApplicationArguments args) {
        addSampleUser("Jan");
        addSampleUser("Andrzej");
        addSampleUser("Tomasz");
    }

    private AppUser addSampleUser(String name) {
        var user = new AppUser();
        user.setName(name);
        user.setEmail(name.toLowerCase() + "@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setAuthProvider(OAuthProvider.NONE);
        return userRepository.save(user);
    }
}
