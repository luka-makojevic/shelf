package com.htec.account.service;

import com.htec.account.entity.EmailVerifyTokenEntity;
import com.htec.account.entity.UserEntity;
import com.htec.account.exception.ShelfException;
import com.htec.account.model.response.TextResponseMessage;
import com.htec.account.repository.mysql.TokenRepository;
import com.htec.account.repository.mysql.UserRepository;
import com.htec.account.util.ErrorMessages;
import com.htec.account.util.TokenGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private RegisterService registerService;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TokenService tokenService;

    private UserEntity userEntity;

    TokenServiceTest() {
        userEntity = new UserEntity();
        userEntity.setId(new Random().nextLong());
        userEntity.setEmail("user@email.com");
    }

    @Test
    void confirmToken() {

        userEntity.setEmailVerified(false);

        String validTestToken = new TokenGenerator(userRepository)
                .generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(validTestToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(2));

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(tokenRepository.findByToken(validTestToken)).thenReturn(Optional.of(tokenEntity));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), eq(null), eq(TextResponseMessage.class))).thenReturn(
                ResponseEntity.status(HttpStatus.OK).body((new TextResponseMessage("", HttpStatus.OK.value())))
        );

        String returnValue = tokenService.confirmToken(validTestToken);

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(1)).findByToken(validTestToken);
        verify(userRepository, times(1)).save(any());
        verify(tokenRepository, times(1)).delete(tokenEntity);

        Assertions.assertEquals(TokenService.EMAIL_CONFIRMED, returnValue);
    }

    @Test
    void confirmToken_TokenNotValid() {

        userEntity.setEmailVerified(false);

        String invalidTestToken = "invalidTestToken";

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(invalidTestToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(2));

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> tokenService.confirmToken(invalidTestToken));

        verify(userRepository, times(0)).findById(any());
        verify(tokenRepository, times(0)).findByToken(invalidTestToken);
        verify(userRepository, times(0)).save(any());
        verify(tokenRepository, times(0)).delete(tokenEntity);

        Assertions.assertEquals(ErrorMessages.TOKEN_NOT_VALID.getErrorMessage(), exception.getMessage());
    }

    @Test
    void confirmToken_UserNotFound() {

        userEntity.setEmailVerified(false);

        String validTestToken = new TokenGenerator(userRepository)
                .generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(validTestToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(2));

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> tokenService.confirmToken(validTestToken));

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(0)).findByToken(validTestToken);
        verify(userRepository, times(0)).save(any());
        verify(tokenRepository, times(0)).delete(tokenEntity);

        Assertions.assertEquals(ErrorMessages.USER_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void confirmToken_EmailAlreadyConfirmed() {

        userEntity.setEmailVerified(true);

        String validTestToken = new TokenGenerator(userRepository)
                .generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(validTestToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(2));

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        String returnValue = tokenService.confirmToken(validTestToken);

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(0)).findByToken(validTestToken);
        verify(userRepository, times(0)).save(any());
        verify(tokenRepository, times(0)).delete(tokenEntity);

        Assertions.assertEquals(TokenService.EMAIL_ALREADY_CONFIRMED, returnValue);
    }

    @Test
    void confirmToken_TokenNotFound() {

        userEntity.setEmailVerified(false);

        String unexcitingToken = new TokenGenerator(userRepository)
                .generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(unexcitingToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(2));

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(tokenRepository.findByToken(unexcitingToken)).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> tokenService.confirmToken(unexcitingToken));

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(1)).findByToken(unexcitingToken);
        verify(userRepository, times(0)).save(any());
        verify(tokenRepository, times(0)).delete(tokenEntity);

        Assertions.assertEquals(ErrorMessages.TOKEN_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void confirmToken_TokenExpired() {

        userEntity.setEmailVerified(false);

        String expiredToken = new TokenGenerator(userRepository)
                .generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(expiredToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().minusMinutes(2));

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(tokenRepository.findByToken(expiredToken)).thenReturn(Optional.of(tokenEntity));

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> tokenService.confirmToken(expiredToken));

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(1)).findByToken(expiredToken);
        verify(userRepository, times(0)).save(any());
        verify(tokenRepository, times(0)).delete(tokenEntity);

        Assertions.assertEquals(ErrorMessages.TOKEN_EXPIRED.getErrorMessage(), exception.getMessage());
    }

    @Test
    void resendToken() {

        userEntity.setEmailVerified(false);

        String validTestToken = new TokenGenerator(userRepository)
                .generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(validTestToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().minusMinutes(2));

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(tokenRepository.findByToken(validTestToken)).thenReturn(Optional.of(tokenEntity));

        String returnValue = tokenService.resendToken(validTestToken);

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(1)).findByToken(validTestToken);
        verify(tokenRepository, times(1)).delete(tokenEntity);
        verify(registerService, times(1)).createAndSendToken(any());

        Assertions.assertEquals(TokenService.TOKEN_RESENT, returnValue);
    }

    @Test
    void resendToken_TokenNotValid() {

        userEntity.setEmailVerified(false);

        String invalidTestToken = "invalidTestToken";

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(invalidTestToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().minusMinutes(2));

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> tokenService.resendToken(invalidTestToken));

        verify(userRepository, times(0)).findById(any());
        verify(tokenRepository, times(0)).findByToken(invalidTestToken);
        verify(tokenRepository, times(0)).delete(tokenEntity);
        verify(registerService, times(0)).createAndSendToken(any());

        Assertions.assertEquals(ErrorMessages.TOKEN_NOT_VALID.getErrorMessage(), exception.getMessage());
    }

    @Test
    void resendToken_UserNotFound() {

        userEntity.setEmailVerified(false);

        String validTestToken = new TokenGenerator(userRepository)
                .generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(validTestToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().minusMinutes(2));

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> tokenService.resendToken(validTestToken));

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(0)).findByToken(validTestToken);
        verify(tokenRepository, times(0)).delete(tokenEntity);
        verify(registerService, times(0)).createAndSendToken(any());

        Assertions.assertEquals(ErrorMessages.USER_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void resendToken_EmailAlreadyConfirmed() {

        userEntity.setEmailVerified(true);

        String validTestToken = new TokenGenerator(userRepository)
                .generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(validTestToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().minusMinutes(2));

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        String returnValue = tokenService.resendToken(validTestToken);

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(0)).findByToken(validTestToken);
        verify(tokenRepository, times(0)).delete(tokenEntity);
        verify(registerService, times(0)).createAndSendToken(any());

        Assertions.assertEquals(TokenService.EMAIL_ALREADY_CONFIRMED, returnValue);
    }

    @Test
    void resendToken_TokenNotFound() {

        userEntity.setEmailVerified(false);

        String unexcitingToken = new TokenGenerator(userRepository)
                .generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(unexcitingToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().minusMinutes(2));

        lenient().when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        lenient().when(tokenRepository.findByToken(unexcitingToken)).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> tokenService.resendToken(unexcitingToken));

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(1)).findByToken(unexcitingToken);
        verify(tokenRepository, times(0)).delete(tokenEntity);
        verify(registerService, times(0)).createAndSendToken(any());

        Assertions.assertEquals(ErrorMessages.TOKEN_NOT_FOUND.getErrorMessage(), exception.getMessage());
    }

    @Test
    void resendToken_TokenNotExpired() {

        userEntity.setEmailVerified(false);

        String expiredToken = new TokenGenerator(userRepository)
                .generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(expiredToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(2));

        lenient().when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        lenient().when(tokenRepository.findByToken(expiredToken)).thenReturn(Optional.of(tokenEntity));

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> tokenService.resendToken(expiredToken));

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(1)).findByToken(expiredToken);
        verify(tokenRepository, times(0)).delete(tokenEntity);
        verify(registerService, times(0)).createAndSendToken(any());

        Assertions.assertEquals(ErrorMessages.TOKEN_NOT_EXPIRED.getErrorMessage(), exception.getMessage());
    }

    @Test
    void confirmToken_FolderNotInitialize() {

        userEntity.setEmailVerified(false);

        String validTestToken = new TokenGenerator(userRepository)
                .generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(validTestToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(2));

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(tokenRepository.findByToken(validTestToken)).thenReturn(Optional.of(tokenEntity));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), eq(null), eq(TextResponseMessage.class))).thenReturn(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((new TextResponseMessage("", HttpStatus.INTERNAL_SERVER_ERROR.value())))
        );

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> tokenService.confirmToken(validTestToken));

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(1)).findByToken(validTestToken);
        verify(userRepository, times(0)).save(any());
        verify(tokenRepository, times(0)).delete(tokenEntity);

        Assertions.assertEquals(ErrorMessages.COULD_NOT_INITIALIZE_FOLDER.getErrorMessage(), exception.getMessage());
    }

    @Test
    void confirmToken_FolderNotInitialize_RestTemplateException() {

        userEntity.setEmailVerified(false);

        String validTestToken = new TokenGenerator(userRepository)
                .generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity tokenEntity = new EmailVerifyTokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setToken(validTestToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(2));

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(tokenRepository.findByToken(validTestToken)).thenReturn(Optional.of(tokenEntity));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), eq(null), eq(TextResponseMessage.class))).thenThrow(
                HttpClientErrorException.class
        );

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> tokenService.confirmToken(validTestToken));

        verify(userRepository, times(1)).findById(any());
        verify(tokenRepository, times(1)).findByToken(validTestToken);
        verify(userRepository, times(0)).save(any());
        verify(tokenRepository, times(0)).delete(tokenEntity);

        Assertions.assertEquals(ErrorMessages.COULD_NOT_INITIALIZE_FOLDER.getErrorMessage(), exception.getMessage());
    }

}