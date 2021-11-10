package com.htec.shelfserver.repository;

import com.htec.shelfserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
