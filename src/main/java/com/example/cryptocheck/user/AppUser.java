package com.example.cryptocheck.user;

import com.example.cryptocheck.auth.oauth.OAuthProvider;
import com.example.cryptocheck.portfolio.PortfolioRecord;
import com.example.cryptocheck.position.Position;
import com.example.cryptocheck.preference.Preference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AppUser {
    @Id
    @SequenceGenerator(
            name="app_user_seq",
            sequenceName = "app_user_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String avatarUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuthProvider authProvider;

    public UserDetails asUserDetails() {
        return new User(email, password, Collections.emptyList());
    }

    @OneToMany(mappedBy = "appUser",
            orphanRemoval = true,
            cascade = CascadeType.REMOVE
    )
    private List<Position> positions;

    @OneToMany(mappedBy = "appUser",
            orphanRemoval = true,
            cascade = CascadeType.REMOVE
    )
    private List<Preference> preferences;

    @OneToMany(mappedBy = "appUser",
            orphanRemoval = true,
            cascade = CascadeType.REMOVE
    )
    private List<PortfolioRecord> portfolioRecords;
}
