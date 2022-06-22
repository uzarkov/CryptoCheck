package com.example.cryptocheck.position;

import com.example.cryptocheck.position.dto.PositionClosure;
import com.example.cryptocheck.position.dto.PositionInput;
import com.example.cryptocheck.position.exception.InvalidPositionException;

import java.time.LocalDate;

public final class PositionValidator {

    private PositionValidator() {

    }

    public static void validateNewPosition(PositionInput position) {
        if (position.quantity() <= 0
                || position.entryPrice() <= 0
                || position.entryDate().isBefore(LocalDate.now())) {
            throw InvalidPositionException.invalidPosition();
        }
    }

    public static void validatePositionClosure(Position targetPosition,
                                 PositionClosure positionClose) {
        var entryDate = targetPosition.getEntryDate();
        var closureDate = positionClose.closureDate();
        if (entryDate.isAfter(closureDate) || positionClose.closurePrice() < 0) {
            throw InvalidPositionException.invalidPosition();
        }
    }
}
