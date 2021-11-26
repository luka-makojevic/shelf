package com.htec.shelfserver.service;

import com.htec.shelfserver.entity.TokenEntity;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.repository.TokenRepository;
import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.security.SecurityConstants;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RegisterService regsterService;

    public final String EMAIL_ALREADY_CONFIRMED = "Email already confirmed";
    public final String EMAIL_CONFIRMED = "Email confirmed";
    public final String TOKEN_RESENT = "Token resent";

    @Autowired
    public TokenService(TokenRepository tokenRepository,
                        UserRepository userRepository,
                        RegisterService regsterService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.regsterService = regsterService;
    }

    @Transactional
    public String confirmToken(String token) {

        String userId;
        try {
            userId = Jwts.parser()
                    .setSigningKey(SecurityConstants.CONFIRMATION_TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getId();
        } catch (JwtException ex) {
            throw ExceptionSupplier.tokenNotValid.get();
        }

        UserEntity userEntity = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(ExceptionSupplier.userNotFound);

        if (userEntity.getEmailVerified()) {
            return EMAIL_ALREADY_CONFIRMED;
        }

        TokenEntity confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(ExceptionSupplier.tokenNotFound);

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw ExceptionSupplier.tokenExpired.get();
        }

        userRepository.enableUser(confirmationToken.getUser().getEmail());

        tokenRepository.delete(confirmationToken);

        return EMAIL_CONFIRMED;
    }

    @Transactional
    public String createAndSendToken(String token) {

        String userId = Jwts.parser()
                .setSigningKey(SecurityConstants.CONFIRMATION_TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getId();

        UserEntity userEntity = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(ExceptionSupplier.userNotFound);

        if (userEntity.getEmailVerified()) {
            return EMAIL_ALREADY_CONFIRMED;
        }

        TokenEntity oldConfirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(ExceptionSupplier.tokenNotFound);

        tokenRepository.delete(oldConfirmationToken);

        regsterService.createAndSendToken(userEntity);

        return TOKEN_RESENT;
    }
}
