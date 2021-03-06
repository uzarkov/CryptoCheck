package com.example.cryptocheck.dev;

import com.example.cryptocheck.auth.oauth.OAuthProvider;
import com.example.cryptocheck.cryptocurrency.Cryptocurrency;

import com.example.cryptocheck.cryptocurrency.CryptocurrencyRepository;
import com.example.cryptocheck.position.Position;
import com.example.cryptocheck.position.PositionRepository;
import com.example.cryptocheck.portfolio.PortfolioRecord;
import com.example.cryptocheck.portfolio.PortfolioRepository;
import com.example.cryptocheck.position.Position;
import com.example.cryptocheck.position.PositionRepository;
import com.example.cryptocheck.position.PositionService;
import com.example.cryptocheck.position.dto.PositionClosure;
import com.example.cryptocheck.preference.Preference;
import com.example.cryptocheck.preference.PreferenceRepository;
import com.example.cryptocheck.user.AppUser;
import com.example.cryptocheck.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Profile("developer")
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final PreferenceRepository preferenceRepository;
    private final PositionRepository positionRepository;
    private final PortfolioRepository portfolioRepository;
    private final PositionService positionService;

    @Override
    public void run(ApplicationArguments args) {
        var user1 = addSampleUser("Jan");
        addSampleUser("Andrzej");
        addSampleUser("Tomasz");
//        var btc = addSampleCrypto("Bitcoin", "BTC", "D");
        addSamplePosition(new BigDecimal("250"), new BigDecimal("1"), LocalDate.of(2022, 2, 10),
                new BigDecimal("300"), LocalDate.of(2022, 5, 25), cryptocurrencyRepository.findByName("Ethereum").orElse(new Cryptocurrency()),
                userRepository.getById(2L));
        addSamplePosition(new BigDecimal("150"), new BigDecimal("2"), LocalDate.of(2022, 5, 11),
                new BigDecimal("320"), LocalDate.of(2022, 5, 15), cryptocurrencyRepository.findByName("Ethereum").orElse(new Cryptocurrency()),
                userRepository.getById(1L));
        addSamplePosition(new BigDecimal("230"), new BigDecimal("3"), LocalDate.of(2022, 4, 10),
                null, null, cryptocurrencyRepository.findByName("Ethereum").orElse(new Cryptocurrency()),
                userRepository.getById(1L));
        addSamplePosition(new BigDecimal("1200"), new BigDecimal("1"), LocalDate.of(2022, 3, 2),
                null, null, cryptocurrencyRepository.findByName("Bitcoin").orElse(new Cryptocurrency()),
                userRepository.getById(2L));
        addSamplePosition(new BigDecimal("10000"), new BigDecimal("2"), LocalDate.of(2022, 2, 7),
                null, null, cryptocurrencyRepository.findByName("Bitcoin").orElse(new Cryptocurrency()),
                userRepository.getById(1L));
        addSamplePosition(new BigDecimal("15000"), new BigDecimal("3"), LocalDate.of(2022, 1, 10),
                new BigDecimal("11000"), LocalDate.of(2022, 2, 9), cryptocurrencyRepository.findByName("Bitcoin").orElse(new Cryptocurrency()),
                userRepository.getById(1L));

        var btc = cryptocurrencyRepository.findByName("Bitcoin").get();
        var sol = cryptocurrencyRepository.findByName("Solana").get();
        var eth = cryptocurrencyRepository.findByName("Ethereum").get();
        var ada = cryptocurrencyRepository.findByName("Cardano").get();
        var dot = cryptocurrencyRepository.findByName("Polkadot").get();
        var matic = cryptocurrencyRepository.findByName("Polygon").get();
        var ltc = cryptocurrencyRepository.findByName("Litecoin").get();
        var link = cryptocurrencyRepository.findByName("Chainlink").get();
        var algo = cryptocurrencyRepository.findByName("Algorand").get();

        addSamplePreference(btc, user1);
        addSamplePreference(sol, user1);
        addSamplePreference(eth, user1);
        addSamplePreference(ada, user1);
        addSamplePreference(matic, user1);
        addSamplePreference(dot, user1);
        addSamplePreference(ltc, user1);
        addSamplePreference(link, user1);
        addSamplePreference(algo, user1);

        addSamplePosition(btc, user1, "1.4223", "20000.567", 7);
        var pos = addSamplePosition(sol, user1, "123.5789", "29.23", 5);
        addSamplePosition(eth, user1, "172.234", "1500.1213", 2);
        addSamplePosition(matic, user1, "13472.12455", "4.24214", 0);

        positionService.close(new PositionClosure(pos.getId(), LocalDate.now(), 40.13), user1.getEmail());

        addSamplePortfolioRecord(sol, user1, "123.23434");
        addSamplePortfolioRecord(btc, user1, "0.324");
        addSamplePortfolioRecord(eth, user1, "3.524");
        addSamplePortfolioRecord(matic, user1, "14224.678");
        addSamplePortfolioRecord(dot, user1, "25.324");
        addSamplePortfolioRecord(link, user1, "102.34");
        addSamplePortfolioRecord(ltc, user1, "5.567");
    }

    private AppUser addSampleUser(String name) {
        var user = new AppUser();
        user.setName(name);
        user.setEmail(name.toLowerCase() + "@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setAuthProvider(OAuthProvider.NONE);
        return userRepository.save(user);
    }

    private Preference addSamplePreference(Cryptocurrency cr, AppUser user) {
        var pref = new Preference(cr, user);
        return preferenceRepository.save(pref);
    }

    private Position addSamplePosition(Cryptocurrency cr, AppUser user, String quantity, String entryPrice, long minusDays) {
        var position = new Position(
                new BigDecimal(entryPrice),
                new BigDecimal(quantity),
                LocalDate.now().minusDays(minusDays),
                cr,
                user
        );
        return positionRepository.save(position);
    }

    private PortfolioRecord addSamplePortfolioRecord(Cryptocurrency cr,
                                                     AppUser user,
                                                     String quantity) {
        var record = new PortfolioRecord(cr, new BigDecimal(quantity), user);
        return portfolioRepository.save(record);
    }

    private Position addSamplePosition(BigDecimal entryPrice, BigDecimal quantity, LocalDate entryDate,
                                       BigDecimal closurePrice, LocalDate closureTime, Cryptocurrency cryptocurrency, AppUser appUser) {
        var position = new Position();
        position.setEntryPrice(entryPrice);
        position.setQuantity(quantity);
        position.setEntryDate(entryDate);
        position.setClosurePrice(closurePrice);
        position.setClosureTime(closureTime);
        position.setCryptocurrency(cryptocurrency);
        position.setAppUser(appUser);
        return positionRepository.save(position);
    }

    private Cryptocurrency addSampleCrypto(String name, String symbol, String coinGekoID) {
        var crypto = new Cryptocurrency();
        crypto.setName(name);
        crypto.setSymbol(symbol);
        crypto.setCoinGeckoId(coinGekoID);
        return cryptocurrencyRepository.save(crypto);
    }
}
