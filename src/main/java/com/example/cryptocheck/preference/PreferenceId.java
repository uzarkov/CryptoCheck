package com.example.cryptocheck.preference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class PreferenceId implements Serializable {

    @Column(name = "app_user_id", nullable = false)
    private Long appUserId;

    @Column(name = "cryptocurrency_id", nullable = false)
    private String cryptocurrencyId;

    private PreferenceId(Long appUserId,
                        String cryptocurrencyId) {
        this.appUserId = appUserId;
        this.cryptocurrencyId = cryptocurrencyId;
    }

    public static PreferenceId from(Long appUserId,
                       String cryptocurrencyId) {
        return new PreferenceId(appUserId, cryptocurrencyId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PreferenceId preferenceId = (PreferenceId) o;
        return Objects.equals(appUserId, preferenceId.getAppUserId())
                && cryptocurrencyId.equals(preferenceId.getCryptocurrencyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(appUserId, cryptocurrencyId);
    }
}
