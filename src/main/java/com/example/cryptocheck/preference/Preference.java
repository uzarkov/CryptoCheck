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

    @EmbeddedId
    private PreferenceId id;

    @ManyToOne
    @MapsId("appUserId")
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @ManyToOne
    @MapsId("cryptocurrencyId")
    @JoinColumn(name = "cryptocurrency_id")
    private Cryptocurrency cryptocurrency;

    public Preference(Cryptocurrency cryptocurrency,
                      AppUser appUser) {
        this.id = PreferenceId.from(appUser.getId(), cryptocurrency.getName());
        this.cryptocurrency = cryptocurrency;
        this.appUser = appUser;
    }
}
