package com.example.cryptocheck.api.price;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BinanceConfig {
    @Bean
    public BinanceApiRestClient restClient() {
        return BinanceApiClientFactory.newInstance().newRestClient();
    }
}
