package com.htec.account.repository.mysql;

import com.htec.account.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.id != ?1 AND u.emailVerified != 0")
    Page<UserEntity> findAll(Long userId, Pageable pageable);

    @Query("SELECT u.pictureName FROM UserEntity u WHERE u.id = :userId")
    Optional<String> getPicturePathByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM UserEntity u WHERE u.id= :userId AND u.role.id != :roleId")
    Optional<UserEntity> getUserByIdAndRoleIdNot(@Param("userId") Long userId,
                                                 @Param("roleId") Long roleId);
}
