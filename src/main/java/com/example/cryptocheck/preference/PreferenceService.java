package com.example.cryptocheck.preference;

import com.example.cryptocheck.cryptocurrency.CryptocurrencyService;
import com.example.cryptocheck.preference.dto.PreferenceInput;
import com.example.cryptocheck.preference.dto.PreferenceOutput;
import com.example.cryptocheck.preference.dto.PricedPreference;
import com.example.cryptocheck.user.AppUser;
import com.example.cryptocheck.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final AppUserService userService;
    private final CryptocurrencyService cryptocurrencyService;

    public List<PreferenceOutput> getUserPreferences(AppUser user) {
        return preferenceRepository
                .findAllById_AppUserId(user.getId())
                .stream()
                .map(PreferenceOutput::from)
                .toList();
    }

    public List<PricedPreference> getPricedUserPreferences(AppUser user,
                                                           Map<String, String> prices) {
        var preferences = getUserPreferences(user);

        var finalPrices = prices.entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleEntry<>(
                        e.getKey().replace("USDT", ""),
                        new BigDecimal(e.getValue())
                ))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        return preferences.stream()
                .map(pref -> PricedPreference.from(pref, finalPrices.get(pref.cryptocurrencyOutput().getSymbol())))
                .toList();
    }

    public List<PreferenceOutput> getUserPreferences(String email) {
        var user = userService.getUserByEmail(email);
        return getUserPreferences(user);
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
