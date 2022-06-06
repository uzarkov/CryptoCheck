package com.example.cryptocheck.price;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.TickerPrice;
import com.example.cryptocheck.cryptocurrency.CryptocurrencyLoader;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PriceService {
    private final BinanceApiRestClient binanceClient;
    @Getter private final Set<String> supportedSymbols;
    @Getter private final Set<String> supportedIntervals;

    public PriceService(BinanceApiRestClient binanceClient,
                        Set<String> supportedSymbols,
                        @Value("${prices.supported-intervals}") Set<String> supportedIntervalsIds) {
        this.binanceClient = binanceClient;
        this.supportedSymbols = supportedSymbols;
        this.supportedIntervals = supportedIntervalsIds;
    }

    @Autowired
    public PriceService(BinanceApiRestClient binanceClient,
                        @Value("${prices.supported-intervals}") Set<String> supportedIntervalsIds) {
        this(binanceClient, CryptocurrencyLoader.cryptoMeta.keySet(), supportedIntervalsIds);
    }

    public List<Candlestick> getCandlestickBarsFor(String symbol, String intervalId) {
        validateSymbol(symbol);
        validateIntervalId(intervalId);

        return binanceClient.getCandlestickBars(symbolWithUsdPair(symbol), candlestickIntervalForId(intervalId));
    }

    private CandlestickInterval candlestickIntervalForId(String intervalId) {
        return Arrays.stream(CandlestickInterval.values())
                .filter(interval -> interval.getIntervalId().equals(intervalId))
                .findFirst()
                .orElseThrow();
    }

    private String symbolWithUsdPair(String symbol) {
        if (symbol.contains("USDT")) {
            return symbol;
        }

        return symbol + "USDT";
    }

    public Map<String, String> getCurrentPricesOf(String symbols) {
        var symbolsArray = symbols.split(",");

        if (symbolsArray.length == 1) {
            var symbol = symbolsArray[0];
            return Map.of(symbol, getCurrentPriceOf(symbol));
        }

        var symbolsSet = Arrays.stream(symbolsArray).collect(Collectors.toSet());
        symbolsSet.forEach(this::validateSymbol);
        var tickerSymbols = symbolsSet.stream().map(this::symbolWithUsdPair).collect(Collectors.toSet());

        return binanceClient.getAllPrices().stream()
                .filter(tickerPrice -> tickerSymbols.contains(tickerPrice.getSymbol()))
                .collect(Collectors.toMap(tickerPrice -> symbolWithoutUsdPair(tickerPrice.getSymbol()),
                                          TickerPrice::getPrice));
    }

    private String symbolWithoutUsdPair(String symbol) {
        var idx = symbol.indexOf("USDT");
        if (idx == -1) {
            return symbol;
        }

        return symbol.substring(0, idx);
    }

    public String getCurrentPriceOf(String symbol) {
        validateSymbol(symbol);

        TickerPrice tickerPrice = binanceClient.getPrice(symbolWithUsdPair(symbol));
        return tickerPrice.getPrice();
    }

    public double getPriceChangeFor(String symbol, String interval) {
        return switch (interval) {
            case "1h" -> get1hPriceChangeFor(symbol);
            case "24h" -> get24hPriceChangeFor(symbol);
            case "7d" -> get7dPriceChangeFor(symbol);
            default -> -1;
        };
    }

    private double get1hPriceChangeFor(String symbol) {
        var candlestics = getCandlestickBarsFor(symbol, "1m");
        var oldPriceCandlestick = candlestics.get(candlestics.size() - 1 - 60);
        var newPriceCandlestick = candlestics.get(candlestics.size() - 1);

        return calculatePriceChange(oldPriceCandlestick, newPriceCandlestick);
    }

    private double get24hPriceChangeFor(String symbol) {
        var candlestics = getCandlestickBarsFor(symbol, "15m");
        var oldPriceCandlestick = candlestics.get(candlestics.size() - 1 - 96);
        var newPriceCandlestick = candlestics.get(candlestics.size() - 1);

        return calculatePriceChange(oldPriceCandlestick, newPriceCandlestick);
    }

    private double get7dPriceChangeFor(String symbol) {
        var candlestics = getCandlestickBarsFor(symbol, "4h");
        var oldPriceCandlestick = candlestics.get(candlestics.size() - 1 - 42);
        var newPriceCandlestick = candlestics.get(candlestics.size() - 1);

        return calculatePriceChange(oldPriceCandlestick, newPriceCandlestick);
    }

    private double calculatePriceChange(Candlestick oldPriceCandlestic, Candlestick newPriceCandlestick) {
        var oldPrice = Double.parseDouble(oldPriceCandlestic.getOpen());
        var newPrice = Double.parseDouble(newPriceCandlestick.getClose());

        var priceDifference = newPrice - oldPrice;
        if (priceDifference == 0) {
            return 0;
        }

        return priceDifference / oldPrice * 100;
    }

    private void validateSymbol(String symbol) {
        if (!supportedSymbols.contains(symbol)) {
            throw PriceServiceException.unsupportedSymbolException(symbol);
        }
    }

    private void validateIntervalId(String intervalId) {
        if (!supportedIntervals.contains(intervalId)) {
            throw PriceServiceException.unsupportedIntervalException(intervalId);
        }
    }
}
