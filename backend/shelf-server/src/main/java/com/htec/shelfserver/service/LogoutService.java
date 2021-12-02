package com.htec.shelfserver.service;

import com.htec.shelfserver.entity.InvalidJwtTokenEntity;
import com.htec.shelfserver.repository.cassandra.InvalidJwtTokenRepository;
import com.htec.shelfserver.repository.mysql.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    private final InvalidJwtTokenRepository invalidJwtTokenRepository;
    private final UserRepository userRepository;

    public LogoutService(InvalidJwtTokenRepository invalidJwtTokenRepository,
                         UserRepository userRepository) {

        this.invalidJwtTokenRepository = invalidJwtTokenRepository;
        this.userRepository = userRepository;
    }

    public void invalidateJwtTokens(String jwtToken, String jwtRefreshToken) {

        InvalidJwtTokenEntity invalidJwtTokenEntity = new InvalidJwtTokenEntity(jwtToken);
        InvalidJwtTokenEntity invalidJwtRefreshTokenEntity = new InvalidJwtTokenEntity(jwtRefreshToken);
        invalidJwtTokenRepository.save(invalidJwtTokenEntity);
        invalidJwtTokenRepository.save(invalidJwtRefreshTokenEntity);
    }
}
