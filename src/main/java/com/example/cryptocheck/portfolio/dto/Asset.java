package com.example.cryptocheck.portfolio.dto;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;

import java.math.BigDecimal;

public record Asset(Cryptocurrency cryptocurrency,
                    BigDecimal quantity) {

    public static Asset from(Cryptocurrency cryptocurrency,
                             BigDecimal quantity) {
        return new Asset(cryptocurrency, quantity);
    }
}
