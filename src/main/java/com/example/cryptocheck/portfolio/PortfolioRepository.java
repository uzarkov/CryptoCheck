package com.example.cryptocheck.portfolio;

import com.example.cryptocheck.cryptocurrency.Cryptocurrency;
import com.example.cryptocheck.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioRecord, Long> {

    List<PortfolioRecord> findAllByAppUser(AppUser appUser);

    List<PortfolioRecord> findAllByAppUserAndCryptocurrency(AppUser appUser, Cryptocurrency cryptocurrency);
}
