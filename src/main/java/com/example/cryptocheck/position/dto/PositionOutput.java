package com.example.cryptocheck.position.dto;

import com.example.cryptocheck.cryptocurrency.dto.CryptocurrencyOutput;
import com.example.cryptocheck.position.Position;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PositionOutput(Long id,
                             CryptocurrencyOutput cryptocurrency,
                             double entryPrice,
                             double quantity,
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                             LocalDate entryDate,
                             double currentPrice,
                             double closurePrice,
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                             LocalDate closureDate,
                             double percentageChange) {

    public static PositionOutput from(Position position,
                                      BigDecimal currentPrice,
                                      BigDecimal percentageChange) {
        var closurePrice = -1d;
        if (position.getClosurePrice() != null) {
            closurePrice = position.getClosurePrice().doubleValue();
        }

        return new PositionOutput(position.getId(),
                CryptocurrencyOutput.from(position.getCryptocurrency()),
                position.getEntryPrice().doubleValue(),
                position.getQuantity().doubleValue(),
                position.getEntryDate(),
                currentPrice.doubleValue(),
                closurePrice,
                position.getClosureTime(),
                percentageChange.doubleValue()
        );
    }
}
