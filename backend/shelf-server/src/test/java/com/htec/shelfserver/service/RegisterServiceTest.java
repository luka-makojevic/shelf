package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.exception.ShelfException;
import com.htec.shelfserver.model.response.UserRegisterMicrosoftResponseModel;
import com.htec.shelfserver.repository.mysql.TokenRepository;
import com.htec.shelfserver.repository.mysql.UserRepository;
import com.htec.shelfserver.util.ErrorMessages;
import com.htec.shelfserver.util.TokenGenerator;
import com.htec.shelfserver.validator.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository confirmationTokenRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private TokenGenerator tokenGenerator;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private EmailService emailService;
    @Mock
    private MicrosoftApiService microsoftApiService;

    @InjectMocks
    private RegisterService registerService;

    @Test
    void registerUser() {

        UserDTO userDTO = new UserDTO();

        userDTO.setId(new Random().nextLong());
        userDTO.setEmail("newUser@email.com");
        userDTO.setPassword("newUser1234!");
        userDTO.setFirstName("newUserFirstName");
        userDTO.setLastName("newUserFirstNameLastName");

        when(userRepository.findByEmail("newUser@email.com")).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenAnswer(invocationOnMock -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail("newUser@email.com");
            userEntity.setId(userDTO.getId());
            return userEntity;
        });

        registerService.registerUser(userDTO);

        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userValidator, times(1)).isUserValid(userDTO);
        verify(tokenGenerator, times(1)).generateSalt(8);
        verify(bCryptPasswordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any());
        verify(tokenGenerator, times(1)).generateConfirmationToken(userDTO.getId());
        verify(confirmationTokenRepository, times(1)).save(any());
        verify(emailService, times(1))
                .sendEmail(eq(userDTO.getEmail()), anyMap(), anyString(), anyString());
    }

    @Test
    void registerUser_RecordAlreadyExists() {

        UserDTO userDTO = new UserDTO();

        userDTO.setEmail("oldUser@email.com");
        userDTO.setPassword("oldUser1234!");
        userDTO.setFirstName("oldUserFirstName");
        userDTO.setLastName("oldUserFirstNameLastName");

        when(userRepository.findByEmail("oldUser@email.com")).thenReturn(Optional.of(new UserEntity()));

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () ->
                registerService.registerUser(userDTO));

        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userValidator, times(0)).isUserValid(userDTO);
        verify(tokenGenerator, times(0)).generateSalt(8);
        verify(bCryptPasswordEncoder, times(0)).encode(anyString());
        verify(userRepository, times(0)).save(any());
        verify(tokenGenerator, times(0)).generateConfirmationToken(userDTO.getId());
        verify(confirmationTokenRepository, times(0)).save(any());
        verify(emailService, times(0))
                .sendEmail(anyString(), anyMap(), anyString(), anyString());

        Assertions.assertEquals(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage(), exception.getMessage());
    }

    @Test
    void registerUserMicrosoft() {

        when(microsoftApiService.getUserInfo("validTestToken"))
                .thenReturn(Optional.of(new UserRegisterMicrosoftResponseModel("newUserGivenName", "newUserSurname", "newUser@email.com")));
        when(userRepository.findByEmail("newUser@email.com")).thenReturn(Optional.empty());

        registerService.registerUserMicrosoft("validTestToken");

        verify(microsoftApiService, times(1)).getUserInfo("validTestToken");
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void registerUserMicrosoft_RecordAlreadyExists() {

        when(microsoftApiService.getUserInfo("validTestToken"))
                .thenReturn(Optional.of(new UserRegisterMicrosoftResponseModel("oldUserGivenName", "oldUserSurname", "oldUser@email.com")));
        when(userRepository.findByEmail("oldUser@email.com")).thenReturn(Optional.of(new UserEntity()));

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> registerService.registerUserMicrosoft("validTestToken"));

        verify(microsoftApiService, times(1)).getUserInfo("validTestToken");
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(0)).save(any());

        Assertions.assertEquals(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage(), exception.getMessage());
    }

    @Test
    void registerUserMicrosoft_AccessTokenExpired() {

        when(microsoftApiService.getUserInfo("expiredTestToken"))
                .thenThrow(ExceptionSupplier.accessTokenNotActive.get());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> registerService.registerUserMicrosoft("expiredTestToken"));

        verify(microsoftApiService, times(1)).getUserInfo("expiredTestToken");
        verify(userRepository, times(0)).findByEmail(anyString());
        verify(userRepository, times(0)).save(any());

        Assertions.assertEquals(ErrorMessages.TOKEN_NOT_ACTIVE.getErrorMessage(), exception.getMessage());
    }

    @Test
    void createAndSendToken() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(new Random().nextLong());
        userEntity.setEmail("testUser@email.com");

        String confirmationTestToken = String.valueOf(new Random().nextInt());

        when(tokenGenerator.generateConfirmationToken(userEntity.getId())).thenReturn(confirmationTestToken);

        registerService.createAndSendToken(userEntity);

        verify(tokenGenerator, times(1)).generateConfirmationToken(anyLong());
        verify(confirmationTokenRepository, times(1)).save(any());
        verify(emailService, times(1))
                .sendEmail(anyString(), anyMap(), anyString(), anyString());
    }
}