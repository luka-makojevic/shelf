package com.htec.shelfserver.repository.mysql;

import com.htec.shelfserver.entity.PasswordResetTokenEntity;
import com.htec.shelfserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    @Transactional
    void deleteAllByUserDetails(UserEntity userEntity);

    Optional<PasswordResetTokenEntity> findByToken(String token);

}
