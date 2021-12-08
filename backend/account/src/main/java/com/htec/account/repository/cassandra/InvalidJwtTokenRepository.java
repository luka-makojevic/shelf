package com.htec.account.repository.cassandra;

import com.htec.account.entity.InvalidJwtTokenEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Optional;

public interface InvalidJwtTokenRepository extends CassandraRepository<InvalidJwtTokenEntity, Long> {

    Optional<InvalidJwtTokenEntity> findByJwt(String jwt);
}
