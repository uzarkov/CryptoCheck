package com.example.cryptocheck.cryptocurrency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, Long> {

    Optional<Cryptocurrency> findByName(String name);
    boolean existsBySymbol(String symbol);

    @Query(value =
            "select symbol " +
            "from ( " +
                "select p.cryptocurrency_id " +
                "from position p " +
                "where p.app_user_id = :userId " +
                        "union " +
                "select pref.cryptocurrency_id " +
                "from preference pref " +
                "where pref.app_user_id = :userId " +
                        "union " +
                "select port.cryptocurrency_id " +
                "from portfolio_record port " +
                "where port.app_user_id = :userId" +
            ")" +
            "join cryptocurrency c on c.name = cryptocurrency_id",
            nativeQuery = true)
    List<String> findUserCryptos(@Param("userId") Long userId);
}
