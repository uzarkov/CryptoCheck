package com.example.cryptocheck.cryptocurrency.dto;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CryptocurrencyOutput {
    private final String name;
    private final String symbol;
    private final String coingeckoId;

    public static CryptocurrencyOutput from(Cryptocurrency cryptocurrency) {
        return new CryptocurrencyOutput(cryptocurrency.getName(),
                                        cryptocurrency.getSymbol(),
                                        cryptocurrency.getCoinGeckoId());
    }
}
