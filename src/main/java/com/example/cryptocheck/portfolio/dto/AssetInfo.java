package com.example.cryptocheck.portfolio.dto;

public record AssetInfo(String cryptocurrencyName,
                        String cryptocurrencySymbol,
                        double quantity,
                        double value,
                        double percentageShare) {

    public static AssetInfo from(AssetShare assetShare) {
        return new AssetInfo(assetShare.cryptocurrency().getName(),
                assetShare.cryptocurrency().getSymbol(),
                assetShare.quantity().doubleValue(),
                assetShare.value().doubleValue(),
                assetShare.percentageShare()
        );
    }

    public boolean isTheSameAs(AssetInfo other) {
        return cryptocurrencyName.equals(other.cryptocurrencyName)
                && cryptocurrencySymbol.equals(other.cryptocurrencySymbol)
                && quantity == other.quantity
                && value == other.value
                && percentageShare == other.percentageShare;
    }
}
