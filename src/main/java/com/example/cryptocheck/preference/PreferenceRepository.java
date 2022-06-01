package com.example.cryptocheck.preference;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;
import com.example.cryptocheck.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, PreferenceId> {

    List<Preference> findAllById_AppUserId(Long appUserId);

    void removePreferenceByAppUserAndCryptocurrency(AppUser user, Cryptocurrency cryptocurrency);
}
