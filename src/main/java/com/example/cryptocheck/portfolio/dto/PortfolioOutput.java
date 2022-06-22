package com.example.cryptocheck.portfolio.dto;

import java.math.BigDecimal;
import java.util.List;

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
        return new PortfolioOutput(Math.round(totalValue * 100.0)/100.0, assets);
    }
}
