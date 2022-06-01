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

        return binanceClient.getCandlestickBars(symbol, candlestickIntervalForId(intervalId));
    }

    public Map<String, String> getCurrentPricesOf(String symbols) {
        var symbolsArray = symbols.split(",");

        if (symbolsArray.length == 1) {
            var symbol = symbolsArray[0];
            return Map.of(symbol, getCurrentPriceOf(symbol));
        }

        var symbolsSet = Arrays.stream(symbolsArray).collect(Collectors.toSet());
        symbolsSet.forEach(this::validateSymbol);

        return binanceClient.getAllPrices().stream()
                .filter(tickerPrice -> symbolsSet.contains(tickerPrice.getSymbol()))
                .collect(Collectors.toMap(TickerPrice::getSymbol, TickerPrice::getPrice));
    }

    public String getCurrentPriceOf(String symbol) {
        validateSymbol(symbol);

        TickerPrice tickerPrice = binanceClient.getPrice(symbol);
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
