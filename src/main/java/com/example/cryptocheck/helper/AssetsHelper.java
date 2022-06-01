package com.example.cryptocheck.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class AssetsHelper {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    private AssetsHelper() {

    }

    public static double computeAssetShare(BigDecimal assetValue,
                                           BigDecimal totalValue) {
        return assetValue.multiply(ONE_HUNDRED)
                .divide(totalValue, 2, RoundingMode.HALF_EVEN)
                .doubleValue();
    }

    public static BigDecimal computeAssetValue(BigDecimal quantity,
                                               BigDecimal price) {
        return quantity.multiply(price);
    }

    public static BigDecimal computePercentageChange(BigDecimal start,
                                                     BigDecimal finish) {
        return (finish.subtract(start))
                .multiply(ONE_HUNDRED)
                .divide(start, 2, RoundingMode.HALF_EVEN);
    }
}
