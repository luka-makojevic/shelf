package com.htec.shelfserver.util;

import com.htec.shelfserver.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Component
public class TokenGenerator {
    private final Random RANDOM = new SecureRandom();

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
                .setSubject(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.CONFIRMATION_TOKEN_SECRET)
                .compact();
    }

}
