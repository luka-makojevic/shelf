package com.htec.account.service;

import com.htec.account.entity.EmailVerifyTokenEntity;
import com.htec.account.entity.UserEntity;
import com.htec.account.exception.ExceptionSupplier;
import com.htec.account.model.response.TextResponseMessage;
import com.htec.account.repository.mysql.TokenRepository;
import com.htec.account.repository.mysql.UserRepository;
import com.htec.account.security.SecurityConstants;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
    public final String DEFAULT_AVATAR_PATH = "../../default-avatar.jpq";

    private RestTemplate restTemplate;

    @Autowired
    public TokenService(TokenRepository tokenRepository,
                        UserRepository userRepository,
                        RegisterService registerService,
                        @Value("${initUserFolderUrl}") String initUserFolderUrl,
                        RestTemplate restTemplate) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.registerService = registerService;
        this.INIT_USER_FOLDER_URL = initUserFolderUrl;
        this.restTemplate = restTemplate;
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

        initializeUserFolders(userEntity);

        userEntity.setEmailVerified(true);

        tokenRepository.delete(confirmationToken);

        userRepository.save(userEntity);

        return EMAIL_CONFIRMED;
    }

    private void initializeUserFolders(UserEntity userEntity) {

        try {
            ResponseEntity<TextResponseMessage> result = restTemplate.exchange(
                    INIT_USER_FOLDER_URL + "/" + userEntity.getId(),
                    HttpMethod.POST,
                    null,
                    TextResponseMessage.class
            );

            if (result.getStatusCode() != HttpStatus.OK) {
                throw ExceptionSupplier.folderNotInitialized.get();
            }

            userEntity.setPictureName(DEFAULT_AVATAR_PATH);

        } catch (HttpClientErrorException ex) {
            throw ExceptionSupplier.folderNotInitialized.get();
        }
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
