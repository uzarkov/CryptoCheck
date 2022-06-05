package com.example.cryptocheck.price;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.TickerPrice;
import lombok.Getter;
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
                        @Value("${prices.supported-symbols}") Set<String> supportedSymbols,
                        @Value("${prices.supported-intervals}") Set<String> supportedIntervalsIds) {
        this.binanceClient = binanceClient;
        this.supportedSymbols = supportedSymbols;
        this.supportedIntervals = supportedIntervalsIds;
    }

    public List<Candlestick> getCandlestickBarsFor(String symbol, String intervalId) {
        validateSymbol(symbol);
        validateIntervalId(intervalId);

        return binanceClient.getCandlestickBars(symbolWithUsdPair(symbol), candlestickIntervalForId(intervalId));
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

    private CandlestickInterval candlestickIntervalForId(String intervalId) {
        return Arrays.stream(CandlestickInterval.values())
                .filter(interval -> interval.getIntervalId().equals(intervalId))
                .findFirst()
                .orElseThrow();
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
