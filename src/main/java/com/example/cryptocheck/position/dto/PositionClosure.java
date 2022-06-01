package com.example.cryptocheck.position.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record PositionClosure(Long id,
                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                              LocalDate closureDate,
                              double closurePrice) {
}
