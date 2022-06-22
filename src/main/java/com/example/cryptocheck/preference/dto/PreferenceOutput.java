package com.example.cryptocheck.preference.dto;

import com.example.cryptocheck.cryptocurrency.dto.CryptocurrencyOutput;
import com.example.cryptocheck.preference.Preference;
import com.example.cryptocheck.preference.PreferenceId;

public record PreferenceOutput(PreferenceId id,
                               CryptocurrencyOutput cryptocurrencyOutput) {

    public static PreferenceOutput from(Preference preference) {
        return new PreferenceOutput(preference.getId(), CryptocurrencyOutput.from(preference.getCryptocurrency()));
    }
}
