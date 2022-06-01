package com.example.cryptocheck.position;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;
import com.example.cryptocheck.user.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Position {

    @Id
    @SequenceGenerator(
            name="position_seq",
            sequenceName = "position_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "position_seq"
    )
    private Long id;

    @Column(nullable = false)
    private BigDecimal entryPrice;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(nullable = false)
    private LocalDate entryDate;

    private BigDecimal closurePrice;

    private LocalDate closureTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cryptocurrency_id")
    private Cryptocurrency cryptocurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    public Position(BigDecimal entryPrice,
                    BigDecimal quantity,
                    LocalDate entryDate,
                    Cryptocurrency cryptocurrency,
                    AppUser appUser) {
        this.entryPrice = entryPrice;
        this.quantity = quantity;
        this.entryDate = entryDate;
        this.cryptocurrency = cryptocurrency;
        this.appUser = appUser;
    }
}
