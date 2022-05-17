package com.example.cryptocheck.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @SequenceGenerator(
            name="user_seq",
            sequenceName = "user_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String password;

    public UserDetails asUserDetails() {
        return new org.springframework.security.core.userdetails.User(email, password, Collections.emptyList());
    }
}
