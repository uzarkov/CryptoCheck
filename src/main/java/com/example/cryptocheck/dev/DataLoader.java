package com.example.cryptocheck.dev;

import com.example.cryptocheck.user.User;
import com.example.cryptocheck.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Profile("developer")
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        addSampleUser("Jan");
        addSampleUser("Andrzej");
        addSampleUser("Tomasz");
    }

    private User addSampleUser(String name) {
        var user = new User();
        user.setName(name);
        user.setEmail(name.toLowerCase(Locale.ROOT) + "@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        return userRepository.save(user);
    }
}
