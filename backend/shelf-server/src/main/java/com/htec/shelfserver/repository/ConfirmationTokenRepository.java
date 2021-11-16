package com.htec.shelfserver.repository;

import com.htec.shelfserver.entity.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Long> {

    ConfirmationTokenEntity findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationTokenEntity c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);
}