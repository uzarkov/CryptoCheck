package com.example.cryptocheck.portfolio

import com.example.cryptocheck.cryptocurrency.Cryptocurrency
import com.example.cryptocheck.cryptocurrency.CryptocurrencyService
import com.example.cryptocheck.portfolio.dto.AssetInfo
import com.example.cryptocheck.portfolio.dto.PortfolioRecordInput
import com.example.cryptocheck.portfolio.exception.NegativeTotalQuantityException
import com.example.cryptocheck.price.PriceService
import com.example.cryptocheck.user.AppUser
import com.example.cryptocheck.user.AppUserService
import spock.lang.Specification

class PortfolioServiceSpec extends Specification {

    def btcName = "Bitcoin"
    def btcSymbol = "BTC"
    def btcCrypto = Mock(Cryptocurrency) {
        getName() >> btcName
        getSymbol() >> btcSymbol
    }

    def ethName = "Ethereum"
    def ethSymbol = "ETH"
    def ethCrypto = Mock(Cryptocurrency) {
        getName() >> ethName
        getSymbol() >> ethSymbol
    }

    def solName = "Solana"
    def solSymbol = "SOL"
    def solCrypto = Mock(Cryptocurrency) {
        getName() >> solName
        getSymbol() >> solSymbol
    }

    def btcQuantity = new BigDecimal("5")
    def ethQuantity = new BigDecimal("30")
    def solQuantity = new BigDecimal("25")

    def btcRecord = Mock(PortfolioRecord) {
        getQuantity() >> btcQuantity
        getCryptocurrency() >> btcCrypto
    }
    def ethRecord = Mock(PortfolioRecord) {
        getQuantity() >> ethQuantity
        getCryptocurrency() >> ethCrypto
    }
    def solRecord = Mock(PortfolioRecord) {
        getQuantity() >> solQuantity
        getCryptocurrency() >> solCrypto
    }

    def userEmail = "jan@test.com"
    def user = Mock(AppUser) {
        getUserEmail() >> userEmail
    }

    def portfolioRepository = Mock(PortfolioRepository)
    def userService = Mock(AppUserService) {
        getUserByEmail(userEmail) >> user
    }
    def cryptocurrencyService = Mock(CryptocurrencyService) {
        getCryptocurrencyById(solName) >> solCrypto
    }
    def priceService = Mock(PriceService) {
        getCurrentPricesOf(_ as String) >> Map.of(
                "BTC", "60",
                "ETH", "5",
                "SOL", "2"
        )
    }
    def portfolioService = new PortfolioService(portfolioRepository, userService, cryptocurrencyService, priceService)

    def "getPortfolio() SHOULD return proper PortfolioOutput for user's portfolio"() {
        given:
        portfolioRepository.findAllByAppUser(user) >> List.of(btcRecord, ethRecord, solRecord)

        when:
        def result = portfolioService.getPortfolio(userEmail)

        then:
        double expectedTotalValue = 500
        double expectedAssetsAmount = 3
        def expBtc = new AssetInfo(btcName, btcSymbol, btcQuantity.doubleValue(), 300, 60)
        def expEth = new AssetInfo(ethName, ethSymbol, ethQuantity.doubleValue(), 150, 30)
        def expSol = new AssetInfo(solName, solSymbol, solQuantity.doubleValue(), 50, 10)
        def expCryptos = Map.of(btcName, expBtc, ethName, expEth, solName, expSol)


        result.totalValue() == expectedTotalValue

        result.assets().size() == expectedAssetsAmount

        for (AssetInfo asset : result.assets()) {
            def expectedAsset = expCryptos.get(asset.cryptocurrencyName())
            assert asset.isTheSameAs(expectedAsset)
        }
    }

    def "addPortfolioRecord() WHEN new PortfolioRecordInput is valid SHOULD save new PortfolioRecord"() {
        given:
        def correctSolAmount = "-25"
        def correctSolQuantity = new BigDecimal(correctSolAmount)
        def correctSolRecord = Mock(PortfolioRecord) {
            getQuantity() >> correctSolQuantity
            getCryptocurrency() >> solCrypto
        }
        def correctPortfolioInput = new PortfolioRecordInput(solName, correctSolAmount)
        portfolioRepository.findAllByAppUser(user) >> List.of(btcRecord,ethRecord, solRecord, correctSolRecord)
        portfolioRepository.findAllByAppUserAndCryptocurrency(user, btcCrypto) >> List.of(btcRecord)
        portfolioRepository.findAllByAppUserAndCryptocurrency(user, ethCrypto) >> List.of(ethRecord)
        portfolioRepository.findAllByAppUserAndCryptocurrency(user, solCrypto) >> List.of(solRecord)

        when:
        def result = portfolioService.addPortfolioRecord(correctPortfolioInput, userEmail)

        then:
        double expectedTotalValue = 450
        double expectedAssetsAmount = 2
        def expBtc = new AssetInfo(btcName, btcSymbol, btcQuantity.doubleValue(), 300, 66.67)
        def expEth = new AssetInfo(ethName, ethSymbol, ethQuantity.doubleValue(), 150, 33.33)
        def expCryptos = Map.of(btcName, expBtc, ethName, expEth)


        result.totalValue() == expectedTotalValue

        result.assets().size() == expectedAssetsAmount

        for (AssetInfo asset : result.assets()) {
            def expectedAsset = expCryptos.get(asset.cryptocurrencyName())
            assert asset.isTheSameAs(expectedAsset)
        }
    }

    def "addPortfolioRecord() WHEN new PortfolioRecordInput is invalid SHOULD throw"() {
        given:
        def incorrectSolAmount = "-25.001"
        def incorrectPortfolioInput = new PortfolioRecordInput(solName, incorrectSolAmount)
          portfolioRepository.findAllByAppUser(user) >> List.of(solRecord)
          portfolioRepository.findAllByAppUserAndCryptocurrency(user, solCrypto) >> List.of(solRecord)

        when:
        portfolioService.addPortfolioRecord(incorrectPortfolioInput, userEmail)

        then:
        thrown(NegativeTotalQuantityException)
    }
}
