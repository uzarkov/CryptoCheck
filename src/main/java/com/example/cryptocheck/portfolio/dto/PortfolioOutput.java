package com.example.cryptocheck.portfolio.dto;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record PortfolioOutput(double totalValue,
                              List<AssetInfo> assets) {

    public static PortfolioOutput from(List<AssetShare> assetsShares) {

        var totalValue = assetsShares.stream()
                .map(AssetShare::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();

        var assets = assetsShares.stream()
                .map(AssetInfo::from)
                .toList();

        return new PortfolioOutput(totalValue, assets);
    }
}
