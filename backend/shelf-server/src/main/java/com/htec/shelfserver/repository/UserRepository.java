package com.htec.shelfserver.repository;

import com.htec.shelfserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u " +
            "SET u.emailVerified = TRUE WHERE u.email = ?1")
    int enableUser(String email);
}
