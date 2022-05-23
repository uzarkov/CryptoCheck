package com.example.cryptocheck.price

import com.binance.api.client.BinanceApiRestClient
import com.binance.api.client.domain.market.Candlestick
import com.binance.api.client.domain.market.CandlestickInterval
import com.binance.api.client.domain.market.TickerPrice
import spock.lang.Specification

class PriceServiceSpec extends Specification {
    def binanceClientMock = Mock(BinanceApiRestClient)

    def "getCandlestickBarsFor() SHOULD return candlestick bars for the given symbol"() {
        given:
        def supportedSymbols = ["BTCUSDT"] as Set<String>
        def supportedIntervals = ["1h"] as Set<String>
        def priceService = new PriceService(binanceClientMock, supportedSymbols, supportedIntervals)

        and:
        1 * binanceClientMock.getCandlestickBars("BTCUSDT", CandlestickInterval.HOURLY) >> [Mock(Candlestick)]

        when:
        def result = priceService.getCandlestickBarsFor("BTCUSDT", "1h")

        then:
        result.size() == 1
    }

    def "getCandlestickBarsFor() WHEN given interval that cannot be mapped to enum SHOULD throw an exception"() {
        given:
        def supportedSymbols = ["BTCUSDT"] as Set<String>
        def supportedIntervals = ["???"] as Set<String>
        def priceService = new PriceService(binanceClientMock, supportedSymbols, supportedIntervals)

        when:
        priceService.getCandlestickBarsFor("BTCUSDT", "???")

        then:
        thrown(NoSuchElementException)
    }

    def "getCandlestickBarsFor() WHEN given unsupported symbol SHOULD throw an exception"() {
        given:
        def supportedSymbols = ["BTCUSDT"] as Set<String>
        def supportedIntervals = ["1h"] as Set<String>
        def priceService = new PriceService(binanceClientMock, supportedSymbols, supportedIntervals)

        when:
        priceService.getCandlestickBarsFor("???", "1h")

        then:
        def ex = thrown(PriceServiceException)
        ex.getMessage() == PriceServiceException.unsupportedSymbolException("???").getMessage()
    }

    def "getCandlestickBarsFor() WHEN given unsupported interval SHOULD throw an exception"() {
        given:
        def supportedSymbols = ["BTCUSDT"] as Set<String>
        def supportedIntervals = ["1h"] as Set<String>
        def priceService = new PriceService(binanceClientMock, supportedSymbols, supportedIntervals)

        when:
        priceService.getCandlestickBarsFor("BTCUSDT", "???")

        then:
        def ex = thrown(PriceServiceException)
        ex.getMessage() == PriceServiceException.unsupportedIntervalException("???").getMessage()
    }

    def "getCurrentPriceOf() SHOULD return current price for the given symbol"() {
        given:
        def supportedSymbols = ["BTCUSDT"] as Set<String>
        def supportedIntervals = [] as Set<String>
        def priceService = new PriceService(binanceClientMock, supportedSymbols, supportedIntervals)

        and:
        1 * binanceClientMock.getPrice("BTCUSDT") >> Mock(TickerPrice) { getPrice() >> "29238.44000000" }

        when:
        def result = priceService.getCurrentPriceOf("BTCUSDT")

        then:
        result == "29238.44000000"
    }

    def "getCurrentPriceOf() WHEN given unsupported symbol SHOULD throw an exception"() {
        given:
        def supportedSymbols = ["BTCUSDT"] as Set<String>
        def supportedIntervals = [] as Set<String>
        def priceService = new PriceService(binanceClientMock, supportedSymbols, supportedIntervals)

        when:
        priceService.getCurrentPriceOf("???")

        then:
        def ex = thrown(PriceServiceException)
        ex.getMessage() == PriceServiceException.unsupportedSymbolException("???").getMessage()
    }
}