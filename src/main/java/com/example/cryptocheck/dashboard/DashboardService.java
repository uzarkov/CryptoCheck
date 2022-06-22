package com.example.cryptocheck.dashboard;

import com.example.cryptocheck.cryptocurrency.CryptocurrencyService;
import com.example.cryptocheck.portfolio.PortfolioService;
import com.example.cryptocheck.position.PositionService;
import com.example.cryptocheck.preference.PreferenceService;
import com.example.cryptocheck.price.PriceService;
import com.example.cryptocheck.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AppUserService userService;
    private final PreferenceService preferenceService;
    private final PositionService positionService;
    private final CryptocurrencyService cryptocurrencyService;
    private final PriceService priceService;
    private final PortfolioService portfolioService;

    public DashboardDTO getDashboardInfo(String userEmail,
                                         int positionsAmount) {
        var user = userService.getUserByEmail(userEmail);
        var userSymbols = cryptocurrencyService.getSymbolsAssociatedWithUser(user.getId());
        var requiredCryptos = String.join(",", userSymbols);
        var prices = priceService.getCurrentPricesOf(requiredCryptos);
        var preferences = preferenceService.getPricedUserPreferences(user, prices);
        var positions = positionService.findLatestPositions(user, prices, positionsAmount);
        var portfolio = portfolioService.getPortfolio(user, prices);

        return DashboardDTO.from(preferences, positions, portfolio);
    }

}
