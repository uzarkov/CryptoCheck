package com.example.cryptocheck.cryptocurrency.dto;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;

public record CryptocurrencyOutput (String name,
                                    String symbol) {

    public static CryptocurrencyOutput from(Cryptocurrency cryptocurrency) {
        return new CryptocurrencyOutput(cryptocurrency.getName(), cryptocurrency.getSymbol());
    }
}
