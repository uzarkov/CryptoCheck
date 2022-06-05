package com.example.cryptocheck.cryptocurrency;

import com.example.cryptocheck.price.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CryptocurrencyLoader implements ApplicationRunner {
    private record CryptoMetadata(String name, String coingeckoId) {}

    private final Map<String, CryptoMetadata> cryptoMeta = Map.ofEntries(
            Map.entry("BTC", new CryptoMetadata("Bitcoin", "bitcoin")),
            Map.entry("ETH", new CryptoMetadata("Ethereum", "ethereum")),
            Map.entry("BNB", new CryptoMetadata("BNB", "binancecoin")),
            Map.entry("ADA", new CryptoMetadata("Cardano", "cardano")),
            Map.entry("XRP", new CryptoMetadata("XRP", "ripple")),
            Map.entry("SOL", new CryptoMetadata("Solana", "solana")),
            Map.entry("DOGE", new CryptoMetadata("Dogecoin", "dogecoin")),
            Map.entry("DOT", new CryptoMetadata("Polkadot", "polkadot")),
            Map.entry("TRX", new CryptoMetadata("TRON", "tron")),
            Map.entry("AVAX", new CryptoMetadata("Avalanche", "avalanche-2")),
            Map.entry("SHIB", new CryptoMetadata("Shiba Inu", "shiba-inu")),
            Map.entry("MATIC", new CryptoMetadata("Polygon", "matic-network")),
            Map.entry("LTC", new CryptoMetadata("Litecoin", "litecoin")),
            Map.entry("NEAR", new CryptoMetadata("NEAR Protocol", "near")),
            Map.entry("UNI", new CryptoMetadata("Uniswap", "uniswap")),
            Map.entry("XLM", new CryptoMetadata("Stellar", "stellar")),
            Map.entry("FTT", new CryptoMetadata("FTX", "ftx-token")),
            Map.entry("LINK", new CryptoMetadata("Chainlink", "chainlink")),
            Map.entry("XMR", new CryptoMetadata("Monero", "monero")),
            Map.entry("ALGO", new CryptoMetadata("Algorand", "algorand")),
            Map.entry("ATOM", new CryptoMetadata("Cosmos Hub", "cosmos"))
    );

    private final PriceService priceService;
    private final CryptocurrencyRepository cryptocurrencyRepository;

    @Override
    public void run(ApplicationArguments args) {
        priceService.getSupportedSymbols().stream()
                .filter(this::symbolDoesNotExist)
                .forEach(this::loadSymbol);
    }

    private boolean symbolDoesNotExist(String symbol) {
        return !cryptocurrencyRepository.existsBySymbol(symbol);
    }

    private void loadSymbol(String symbol) {
        if (!cryptoMeta.containsKey(symbol)) {
            return;
        }

        var cryptocurrencyMetadata = cryptoMeta.get(symbol);
        cryptocurrencyRepository.save(new Cryptocurrency(cryptocurrencyMetadata.name(),
                                                         symbol,
                                                         cryptocurrencyMetadata.coingeckoId()));
    }
}
