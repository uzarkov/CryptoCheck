package com.example.cryptocheck.preference;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;
import com.example.cryptocheck.user.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Preference {

    @Id
    @SequenceGenerator(
            name="preference_seq",
            sequenceName = "preference_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "preference_seq"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cryptocurrency_id")
    private Cryptocurrency cryptocurrency;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    public Preference(Cryptocurrency cryptocurrency,
                      AppUser appUser) {
        this.cryptocurrency = cryptocurrency;
        this.appUser = appUser;
    }
}
