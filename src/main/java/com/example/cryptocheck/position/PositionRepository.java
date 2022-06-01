package com.example.cryptocheck.position;

import com.example.cryptocheck.user.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    Page<Position> findAllByAppUser(AppUser appUser, Pageable pageable);

    Optional<Position> findFirstById(Long id);
}
