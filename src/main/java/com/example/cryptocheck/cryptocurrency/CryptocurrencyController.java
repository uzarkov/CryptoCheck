package com.example.cryptocheck.cryptocurrency;

import com.example.cryptocheck.cryptocurrency.dto.CryptocurrencyOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cryptocurrency")
public class CryptocurrencyController {

    private final CryptocurrencyService cryptocurrencyService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CryptocurrencyOutput>> getAllCryptoCurrencies(Pageable pageable) {
        var cryptocurrencies = cryptocurrencyService.getAllCryptoCurrencies(pageable);
        return ResponseEntity.ok().body(cryptocurrencies);
    }

    @GetMapping(value = "/{cryptoId}")
    public ResponseEntity<Cryptocurrency> getCryptoCurrencyById(@PathVariable("cryptoId") String name) {
        var crypto = cryptocurrencyService.getCryptocurrencyById(name);
        return ResponseEntity.ok().body(crypto);
    }
}
