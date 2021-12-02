package com.htec.shelfserver.repository.cassandra;

import com.htec.shelfserver.entity.InvalidJwtTokenEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Optional;

public interface InvalidJwtTokenRepository extends CassandraRepository<InvalidJwtTokenEntity, Long> {

    Optional<InvalidJwtTokenEntity> findByJwt(String jwt);
}
