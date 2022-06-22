package com.example.cryptocheck.dashboard;

import com.example.cryptocheck.portfolio.dto.PortfolioOutput;
import com.example.cryptocheck.position.dto.PositionOutput;
import com.example.cryptocheck.preference.dto.PricedPreference;

import java.util.List;

public record DashboardDTO(List<PricedPreference> preferences,
                           List<PositionOutput> positions,
                           PortfolioOutput portfolio) {
    public static DashboardDTO from(List<PricedPreference> preferences,
                                    List<PositionOutput> positions,
                                    PortfolioOutput portfolio) {
        return new DashboardDTO(preferences, positions, portfolio);
    }
}
