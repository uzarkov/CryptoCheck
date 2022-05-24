package com.example.cryptocheck.price;

import com.binance.api.client.domain.market.Candlestick;

public record CandlestickOutput(Long openTime, String closePrice) {
    public static CandlestickOutput from(Candlestick candlestick) {
        return new CandlestickOutput(candlestick.getOpenTime(),
                                     candlestick.getClose());
    }
}
