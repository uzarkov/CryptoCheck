package com.example.cryptocheck.preference.dto;

import com.example.cryptocheck.preference.Preference;

public record PreferenceOutput(String name,
                               String symbol) {

    public static PreferenceOutput from(Preference preference) {
        return new PreferenceOutput(preference.getCryptocurrency().getName(),
                preference.getCryptocurrency().getSymbol());
    }
}
