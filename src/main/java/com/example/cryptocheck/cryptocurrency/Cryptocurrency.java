package com.example.cryptocheck.cryptocurrency;

import com.example.cryptocheck.portfolio.PortfolioRecord;
import com.example.cryptocheck.position.Position;
import com.example.cryptocheck.preference.Preference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Cryptocurrency {

    @Id
    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 6, unique = true)
    private String symbol;

    @Column(nullable = false, length = 30, unique = true)
    private String coinGeckoId;

    @OneToMany(mappedBy = "cryptocurrency")
    private List<Preference> preferences;

    @OneToMany(mappedBy = "cryptocurrency")
    private List<PortfolioRecord> portfolioRecords;

    @OneToMany(mappedBy = "cryptocurrency")
    private List<Position> positions;

    public Cryptocurrency(String name,
                          String symbol,
                          String coinGeckoId) {
        this.name = name;
        this.symbol = symbol;
        this.coinGeckoId = coinGeckoId;
        this.preferences = new ArrayList<>();
        this.portfolioRecords = new ArrayList<>();
        this.positions = new ArrayList<>();
    }
}
