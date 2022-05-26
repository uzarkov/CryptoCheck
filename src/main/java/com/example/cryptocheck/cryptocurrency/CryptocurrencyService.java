package com.example.cryptocheck.cryptocurrency;

import com.example.cryptocheck.cryptocurrency.dto.CryptocurrencyOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CryptocurrencyService {

    private final CryptocurrencyRepository cryptocurrencyRepository;

    public Page<CryptocurrencyOutput> getAllCryptoCurrencies(Pageable pageable) {
        return cryptocurrencyRepository.findAll(pageable)
                .map(CryptocurrencyOutput::from);
    }

    public Cryptocurrency getCryptocurrencyById(String name) {
        return cryptocurrencyRepository
                .findByName(name)
                .orElseThrow(NoSuchCryptocurrencyException::invalid);
    }
}
