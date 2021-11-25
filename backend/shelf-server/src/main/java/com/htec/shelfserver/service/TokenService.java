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
import java.util.Optional;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public final String EMAIL_ALREADY_CONFIRMED = "Email already confirmed";
    public final String EMAIL_CONFIRMED = "Email confirmed";

    @Autowired
    public TokenService(TokenRepository tokenRepository,
                        UserRepository userRepository,
                        UserService userService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.userService = userService;
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

        Optional<UserEntity> userEntityOptional = userRepository.findById(Long.parseLong(userId));

        if (!userEntityOptional.isPresent()) {
            throw ExceptionSupplier.userNotFound.get();
        }

        if (userEntityOptional.get().getEmailVerified()) {
            return EMAIL_ALREADY_CONFIRMED;
        }

        Optional<TokenEntity> confirmationToken = tokenRepository.findByToken(token);

        if (!confirmationToken.isPresent()) {
            throw ExceptionSupplier.tokenNotFound.get();
        }

        LocalDateTime expiredAt = confirmationToken.get().getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw ExceptionSupplier.tokenExpired.get();
        }

        userRepository.enableUser(confirmationToken.get().getUser().getEmail());

        tokenRepository.delete(confirmationToken.get());

        return EMAIL_CONFIRMED;
    }

    @Transactional
    public String createAndSendToken(String token) {

        String userId = Jwts.parser()
                .setSigningKey(SecurityConstants.CONFIRMATION_TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getId();

        Optional<UserEntity> userEntityOptional = userRepository.findById(Long.parseLong(userId));

        if (!userEntityOptional.isPresent()) {
            throw ExceptionSupplier.userNotFound.get();
        }

        if (userEntityOptional.get().getEmailVerified()) {
            return "Email already confirmed";
        }

        Optional<TokenEntity> oldConfirmationTokenOptional = tokenRepository.findByToken(token);

        if (!oldConfirmationTokenOptional.isPresent()) {
            throw ExceptionSupplier.tokenNotFound.get();
        }

        tokenRepository.delete(oldConfirmationTokenOptional.get());

        userService.createAndSendToken(userEntityOptional.get());

        return "Token resent";

    }
}
