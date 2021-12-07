package com.htec.account.service;

import com.htec.account.entity.InvalidJwtTokenEntity;
import com.htec.account.repository.cassandra.InvalidJwtTokenRepository;
import com.htec.account.repository.mysql.UserRepository;
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
