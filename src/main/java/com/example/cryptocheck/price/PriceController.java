package com.example.cryptocheck.price;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/prices")
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    @GetMapping(path = "/symbols")
    public Set<String> getSupportedSymbols() {
        return priceService.getSupportedSymbols();
    }

    @GetMapping(path = "/intervals")
    public Set<String> getSupportedIntervals() {
        return priceService.getSupportedIntervals();
    }

    @GetMapping(path = "/{symbol}")
    public String getCurrentPrice(@PathVariable(name = "symbol") String symbol) {
        return priceService.getCurrentPriceOf(symbol);
    }

    @GetMapping(path = "/{symbol}/{intervalId}")
    public List<CandlestickOutput> getCandlestickBars(@PathVariable(name = "symbol") String symbol,
                                                @PathVariable(name = "intervalId") String intervalId) {
        return priceService.getCandlestickBarsFor(symbol, intervalId).stream()
                .map(CandlestickOutput::from)
                .toList();
    }
}
