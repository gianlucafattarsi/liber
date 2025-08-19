package com.github.gianlucafattarsi.liberapi.context.account.user.repository;

import com.github.gianlucafattarsi.liberapi.context.account.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByIdAndExpiresAtAfter(UUID id, Instant date);

    void deleteByUserId(long userId);
}