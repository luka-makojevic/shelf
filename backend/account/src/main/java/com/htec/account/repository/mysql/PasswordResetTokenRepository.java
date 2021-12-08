package com.htec.account.repository.mysql;

import com.htec.account.entity.PasswordResetTokenEntity;
import com.htec.account.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    @Transactional
    void deleteAllByUserDetails(UserEntity userEntity);

    Optional<PasswordResetTokenEntity> findByToken(String token);

}
