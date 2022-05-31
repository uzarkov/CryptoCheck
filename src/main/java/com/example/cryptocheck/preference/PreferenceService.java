package com.example.cryptocheck.preference;

import com.example.cryptocheck.cryptocurrency.CryptocurrencyService;
import com.example.cryptocheck.preference.dto.PreferenceInput;
import com.example.cryptocheck.preference.dto.PreferenceOutput;
import com.example.cryptocheck.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final AppUserService userService;
    private final CryptocurrencyService cryptocurrencyService;

    public List<PreferenceOutput> getUserPreferences(String email) {
        var user = userService.getUserByEmail(email);
        return preferenceRepository
                .findAllById_AppUserId(user.getId())
                .stream()
                .map(PreferenceOutput::from)
                .toList();
    }

    @Transactional
    public PreferenceOutput addPreference(PreferenceInput preferenceInput,
                                          String userEmail) {
        var cryptocurrencyName = preferenceInput.cryptocurrencyName();
        var cryptocurrency = cryptocurrencyService.getCryptocurrencyById(cryptocurrencyName);
        var user = userService.getUserByEmail(userEmail);
        var preference = preferenceRepository.save(new Preference(cryptocurrency, user));
        return PreferenceOutput.from(preference);
    }

    @Transactional
    public void removePreference(String cryptocurrencyName,
                                 String userEmail) {
        var cryptocurrency = cryptocurrencyService.getCryptocurrencyById(cryptocurrencyName);
        var user = userService.getUserByEmail(userEmail);
        preferenceRepository.removePreferenceByAppUserAndCryptocurrency(user, cryptocurrency);
    }
}
