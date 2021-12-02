package com.htec.shelfserver.util;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.repository.mysql.UserRepository;
import com.htec.shelfserver.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Component
public class TokenGenerator {
    private final Random RANDOM = new SecureRandom();
    private final UserRepository userRepository;

    public TokenGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateSalt(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnValue);
    }

    public String generateConfirmationToken(Long userId) {
        return Jwts.builder()
                .setId(userId.toString())
                .claim(JwtFieldConstants.TOKEN_ID, UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.CONFIRMATION_TOKEN_SECRET)
                .compact();
    }

    public String generatePasswordResetToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .compact();
    }

    public String generateJwtToken(UserDTO userDTO) {

        UserEntity userEntity = userRepository.findByEmail(userDTO.getEmail()).orElseThrow(ExceptionSupplier.recordNotFoundWithEmail);
        return Jwts.builder()
                .setId(userEntity.getId().toString())
                .setSubject(userEntity.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.JWT_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .claim(JwtFieldConstants.ROLE_ID, userEntity.getRole().getId())
                .compact();
    }

    public String generateJwtRefreshToken(UserDTO userDTO) {

        UserEntity userEntity = userRepository.findByEmail(userDTO.getEmail()).orElseThrow(ExceptionSupplier.recordNotFoundWithEmail);
        return Jwts.builder()
                .setId(userEntity.getId().toString())
                .setSubject(userEntity.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.REFRESH_JWT_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .claim(JwtFieldConstants.ROLE_ID, userEntity.getRole().getId())
                .compact();
    }
}
