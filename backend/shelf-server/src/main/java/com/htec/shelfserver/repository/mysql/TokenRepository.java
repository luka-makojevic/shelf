package com.htec.shelfserver.repository.mysql;

import com.htec.shelfserver.entity.EmailVerifyTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<EmailVerifyTokenEntity, Long> {

    Optional<EmailVerifyTokenEntity> findByToken(String token);

}