package com.example.cryptocheck.cryptocurrency;

import com.example.cryptocheck.portfolio.Portfolio;
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
    @SequenceGenerator(
            name="currency_seq",
            sequenceName = "currency_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "currency_seq"
    )
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String name;

    @Column(nullable = false, length = 6, unique = true)
    private String symbol;

    @OneToMany(mappedBy = "cryptocurrency")
    private List<Preference> preferences;

    @OneToMany(mappedBy = "cryptocurrency")
    private List<Portfolio> portfolios;

    @OneToMany(mappedBy = "cryptocurrency")
    private List<Position> positions;

    public Cryptocurrency(String name,
                          String symbol) {
        this.name = name;
        this.symbol = symbol;
        this.preferences = new ArrayList<>();
        this.portfolios = new ArrayList<>();
        this.positions = new ArrayList<>();
    }
}
