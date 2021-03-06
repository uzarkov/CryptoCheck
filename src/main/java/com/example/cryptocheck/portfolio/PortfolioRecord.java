package com.example.cryptocheck.portfolio;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;
import com.example.cryptocheck.user.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class PortfolioRecord {

    @Id
    @SequenceGenerator(
            name="portfolio_record_seq",
            sequenceName = "portfolio_record_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "portfolio_record_seq"
    )
    private Long id;

    @Column(nullable = false)
    private BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "cryptocurrency_id")
    private Cryptocurrency cryptocurrency;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    public PortfolioRecord(Cryptocurrency cryptocurrency,
                           BigDecimal quantity,
                           AppUser appUser) {
        this.cryptocurrency = cryptocurrency;
        this.appUser = appUser;
        this.quantity = quantity;
    }
}
