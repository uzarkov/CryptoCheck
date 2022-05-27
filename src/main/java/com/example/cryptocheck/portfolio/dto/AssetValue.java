package com.example.cryptocheck.portfolio.dto;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;

import java.math.BigDecimal;

public record AssetValue(Cryptocurrency cryptocurrency,
                         BigDecimal quantity,
                         BigDecimal value) {

    public static AssetValue from(Asset asset,
                                  BigDecimal value) {
        return new AssetValue(asset.cryptocurrency(), asset.quantity(), value);
    }
}
