package com.example.cryptocheck.cryptocurrency.dto;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;
import lombok.Getter;

@Getter
public class PriceAwareCryptocurrencyOutput extends CryptocurrencyOutput {
    private final String price;

    private PriceAwareCryptocurrencyOutput(Cryptocurrency cryptocurrency, String price) {
        super(cryptocurrency.getName(), cryptocurrency.getSymbol(), cryptocurrency.getCoinGeckoId());
        this.price = price;
    }

    public static PriceAwareCryptocurrencyOutput from(Cryptocurrency cryptocurrency, String price) {
        return new PriceAwareCryptocurrencyOutput(cryptocurrency, price);
    }
}
