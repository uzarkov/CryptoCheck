package com.example.cryptocheck.preference.dto;

import com.example.cryptocheck.cryptocurrency.dto.CryptocurrencyOutput;
import com.example.cryptocheck.preference.PreferenceId;

import java.math.BigDecimal;
import java.util.Random;

public record PricedPreference(PreferenceId id,
                               CryptocurrencyOutput cryptocurrencyOutput,
                               double price,
                               double change) {

    public static PricedPreference from(PreferenceOutput prefOutput,
                                        BigDecimal price) {
        var r = new Random();
        return new PricedPreference(prefOutput.id(),
                prefOutput.cryptocurrencyOutput(), price.doubleValue(),
                Math.round((-10 + 20 * r.nextDouble())*100)/100.0);
    }
}
