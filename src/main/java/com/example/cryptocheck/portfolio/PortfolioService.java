package com.example.cryptocheck.portfolio;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;
import com.example.cryptocheck.cryptocurrency.CryptocurrencyService;
import com.example.cryptocheck.helper.AssetsHelper;
import com.example.cryptocheck.portfolio.dto.*;
import com.example.cryptocheck.portfolio.exception.NegativeTotalQuantityException;
import com.example.cryptocheck.price.PriceService;
import com.example.cryptocheck.user.AppUser;
import com.example.cryptocheck.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final AppUserService userService;
    private final CryptocurrencyService cryptocurrencyService;
    private final PriceService priceService;

    public PortfolioOutput getPortfolio(AppUser user,
                                        Map<String, String> prices) {
        return buildPortfolio(user, prices);
    }

    public PortfolioOutput getPortfolio(String userEmail) {
        var user = userService.getUserByEmail(userEmail);
        return buildPortfolio(user, new HashMap<>());
    }

    public PortfolioOutput getPortfolio(AppUser user) {
        return buildPortfolio(user, new HashMap<>());
    }

    @Transactional
    public PortfolioOutput addPortfolioRecord(PortfolioRecordInput portfolioRecordInput,
                                              String userEmail) {
        var user = userService.getUserByEmail(userEmail);
        var newRecord = toPortfolioRecord(portfolioRecordInput, user);
        verifyRecord(newRecord, user);
        portfolioRepository.save(newRecord);

        return getPortfolio(user);
    }

    private PortfolioOutput buildPortfolio(AppUser user,
                                           Map<String, String> prices) {
        var userAssets = getUserAssets(user);
        var assetsShares = findAssetsShare(userAssets, prices);

        return PortfolioOutput.from(assetsShares);
    }

    private List<Asset> getUserAssets(AppUser user) {
        return portfolioRepository
                .findAllByAppUser(user)
                .stream()
                .collect(Collectors.groupingBy(
                        PortfolioRecord::getCryptocurrency,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                PortfolioRecord::getQuantity,
                                BigDecimal::add
                        )
                ))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().compareTo(BigDecimal.ZERO) != 0)
                .map(e -> Asset.from(e.getKey(), e.getValue()))
                .toList();
    }

    private List<AssetShare> findAssetsShare(List<Asset> assets,
                                             Map<String, String> prices) {
        var assetsValues = findAssetsPrices(assets, prices);

        var totalValue = assetsValues.stream()
                .map(AssetValue::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return assetsValues.stream()
                .map(asset -> AssetShare.from(asset, AssetsHelper.computeAssetShare(asset.value(), totalValue)))
                .toList();
    }

    private List<AssetValue> findAssetsPrices(List<Asset> assets,
                                              Map<String, String> prices) {
        if (!assets.isEmpty() && prices.isEmpty()) {
            prices = getRequiredPrices(assets);
        }
        var finalPrices = prices;

        return assets.stream()
                .map(asset -> {
                    var price = finalPrices.get(asset.cryptocurrency().getSymbol());
                    var val = AssetsHelper.computeAssetValue(asset.quantity(), new BigDecimal(price));
                    return AssetValue.from(asset, val);
                })
                .toList();
    }

    private void verifyRecord(PortfolioRecord record,
                              AppUser user) {
        var totalQuantity = findTotalQuantityOfSpecificCrypto(record.getCryptocurrency(), user);

        if (isQuantityReducedBelowZero(totalQuantity, record.getQuantity())) {
            throw NegativeTotalQuantityException.invalidQuantity();
        }
    }

    private BigDecimal findTotalQuantityOfSpecificCrypto(Cryptocurrency cryptocurrency,
                                                         AppUser user) {
        return portfolioRepository
                .findAllByAppUserAndCryptocurrency(user, cryptocurrency)
                .stream()
                .map(PortfolioRecord::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isQuantityReducedBelowZero(BigDecimal totalQuantity,
                                               BigDecimal proposedQuantity) {
        return totalQuantity.add(proposedQuantity).compareTo(BigDecimal.ZERO) < 0;
    }

    private PortfolioRecord toPortfolioRecord(PortfolioRecordInput portfolioRecordInput,
                                              AppUser user) {
        var cryptocurrencyName = portfolioRecordInput.cryptocurrencyName();
        var cryptocurrency =  cryptocurrencyService.getCryptocurrencyById(cryptocurrencyName);
        var quantity = new BigDecimal(portfolioRecordInput.quantity());

        return new PortfolioRecord(cryptocurrency, quantity, user);
    }

    private Map<String, String> getRequiredPrices(List<Asset> assets) {
        var inPairWith = "USDT";
        var requiredCryptos = assets.stream()
                .map(asset -> asset.cryptocurrency().getSymbol().concat(inPairWith))
                .collect(Collectors.joining(","));

        return priceService
                .getCurrentPricesOf(requiredCryptos)
                .entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey().replace(inPairWith, ""), e.getValue()))
                .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue
                ));
    }
}
