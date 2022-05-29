package com.example.cryptocheck.portfolio.dto;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;

import java.math.BigDecimal;

public record AssetShare(Cryptocurrency cryptocurrency,
                         BigDecimal quantity,
                         BigDecimal value,
                         Double percentageShare) {

    public static AssetShare from(AssetValue asset,
                                  Double percentageShare) {
        return new AssetShare(asset.cryptocurrency(), asset.quantity(), asset.value(), percentageShare);
    }
}
