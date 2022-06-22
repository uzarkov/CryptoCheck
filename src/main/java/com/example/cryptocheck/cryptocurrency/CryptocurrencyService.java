package com.example.cryptocheck.cryptocurrency;

import com.example.cryptocheck.cryptocurrency.dto.CryptocurrencyOutput;
import com.example.cryptocheck.cryptocurrency.dto.PriceAwareCryptocurrencyOutput;
import com.example.cryptocheck.price.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CryptocurrencyService {
    private final PriceService priceService;
    private final CryptocurrencyRepository cryptocurrencyRepository;

    public Page<CryptocurrencyOutput> getAllCryptoCurrencies(Pageable pageable) {
        var page = cryptocurrencyRepository.findAll(pageable);
        var cryptocurrencies = page.getContent();
        var prices = getPricesFor(cryptocurrencies);

        return page.map(crypto -> PriceAwareCryptocurrencyOutput.from(crypto, prices.get(crypto.getSymbol())));
    }

    public List<String> getSymbolsAssociatedWithUser(Long userId) {
        return cryptocurrencyRepository.findUserCryptos(userId);
    }

    private Map<String, String> getPricesFor(List<Cryptocurrency> cryptocurrencies) {
        var symbols = cryptocurrencies.stream()
                .map(Cryptocurrency::getSymbol)
                .toList();
        return priceService.getCurrentPricesOf(String.join(",", symbols));
    }

    public Cryptocurrency getCryptocurrencyById(String name) {
        return cryptocurrencyRepository
                .findByName(name)
                .orElseThrow(NoSuchCryptocurrencyException::invalid);
    }
}
