package com.htec.account.repository.mysql;

import com.htec.account.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u " +
            "SET u.emailVerified = TRUE WHERE u.email = ?1")
    int enableUser(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.id != ?1 AND u.emailVerified != 0")
    Page<UserEntity> findAll(Long userId, Pageable pageable);
}
