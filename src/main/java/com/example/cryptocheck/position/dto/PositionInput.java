package com.example.cryptocheck.position.dto;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;
import com.example.cryptocheck.position.Position;
import com.example.cryptocheck.user.AppUser;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PositionInput(String cryptocurrencyName,
                            double entryPrice,
                            double quantity,
                            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                            LocalDate entryDate) {

    public Position toPosition(AppUser user,
                               Cryptocurrency cryptocurrency) {

        return new Position(
                BigDecimal.valueOf(entryPrice),
                BigDecimal.valueOf(quantity),
                entryDate,
                cryptocurrency,
                user
        );
    }
}
