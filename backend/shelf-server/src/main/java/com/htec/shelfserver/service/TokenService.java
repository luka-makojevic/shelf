package com.htec.shelfserver.service;

import com.htec.shelfserver.entity.EmailVerifyTokenEntity;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.repository.mysql.TokenRepository;
import com.htec.shelfserver.repository.mysql.UserRepository;
import com.htec.shelfserver.security.SecurityConstants;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RegisterService registerService;

    public static final String EMAIL_ALREADY_CONFIRMED = "Email already confirmed";
    public static final String EMAIL_CONFIRMED = "Email confirmed";
    public static final String TOKEN_RESENT = "Token resent";
    public final String INIT_USER_FOLDER_URL;

    @Autowired
    public TokenService(TokenRepository tokenRepository,
                        UserRepository userRepository,
                        RegisterService registerService,
                        @Value("${initUserFolderUrl}") String initUserFolderUrl) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.registerService = registerService;
        this.INIT_USER_FOLDER_URL = initUserFolderUrl;
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

        EmailVerifyTokenEntity confirmationToken = tokenRepository.findByToken(token)
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
    public String resendToken(String token) {

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

        EmailVerifyTokenEntity oldConfirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(ExceptionSupplier.tokenNotFound);

        LocalDateTime expiredAt = oldConfirmationToken.getExpiresAt();

        if (expiredAt.isAfter(LocalDateTime.now())) {
            throw ExceptionSupplier.tokenNotExpired.get();
        }

        tokenRepository.delete(oldConfirmationToken);

        registerService.createAndSendToken(userEntity);

        return TOKEN_RESENT;
    }
}
