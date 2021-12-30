package com.htec.account.service;

import com.htec.account.dto.UserDTO;
import com.htec.account.entity.EmailVerifyTokenEntity;
import com.htec.account.entity.RoleEntity;
import com.htec.account.entity.UserEntity;
import com.htec.account.exception.ExceptionSupplier;
import com.htec.account.mapper.UserMapper;
import com.htec.account.model.response.UserRegisterMicrosoftResponseModel;
import com.htec.account.repository.mysql.TokenRepository;
import com.htec.account.repository.mysql.UserRepository;
import com.htec.account.util.TokenGenerator;
import com.htec.account.validator.UserValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final TokenRepository confirmationTokenRepository;

    private final TokenGenerator tokenGenerator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final MicrosoftApiService microsoftApiService;
    private final UserValidator userValidator;
    private final Long FREE_SPACE_SIZE;
    public final String DEFAULT_AVATAR_PATH = "default-avatar.jpg";

    private final String emailVerificationLink;

    public RegisterService(UserRepository userRepository,
                           TokenRepository confirmationTokenRepository,
                           TokenGenerator tokenGenerator,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           EmailService emailService,
                           MicrosoftApiService microsoftApiService,
                           UserValidator userValidator,
                           @Value("${emailVerificationLink}") String emailVerificationLink,
                           @Value("${userFreeSpace}") Long free_space_size) {

        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.tokenGenerator = tokenGenerator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.microsoftApiService = microsoftApiService;
        this.userValidator = userValidator;
        this.emailVerificationLink = emailVerificationLink;
        this.FREE_SPACE_SIZE = free_space_size;
    }

    public void registerUser(UserDTO userDTO) {

        userRepository.findByEmail(userDTO.getEmail()).ifPresent(
                userEntity -> {
                    throw ExceptionSupplier.recordAlreadyExists.get();
                });

        userValidator.isUserValid(userDTO);

        UserEntity userEntity = UserMapper.INSTANCE.userDtoToUserEntity(userDTO);

        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setEmailVerified(false);
        userEntity.setRole(new RoleEntity(3L));
        userEntity.setFreeSpace(FREE_SPACE_SIZE);
        userEntity.setPictureName(DEFAULT_AVATAR_PATH);

        String salt = tokenGenerator.generateSalt(8);
        userEntity.setSalt(salt);

        String encryptedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword() + salt);
        userEntity.setPassword(encryptedPassword);

        UserEntity storedUser = userRepository.save(userEntity);
        createAndSendToken(storedUser);
    }

    public void registerUserMicrosoft(String bearerToken) {

        UserRegisterMicrosoftResponseModel response = microsoftApiService.getUserInfo(bearerToken)
                .orElseThrow(ExceptionSupplier.accessTokenNotActive);

        userRepository.findByEmail(response.getMail()).ifPresent(
                userEntity -> {
                    throw ExceptionSupplier.recordAlreadyExists.get();
                });

        UserEntity userEntity = UserMapper.INSTANCE.userRegisterMicrosoftResponseModelToUserEntity(response);

        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setEmailVerified(true);
        userEntity.setRole(new RoleEntity(3L));
        userEntity.setFreeSpace(FREE_SPACE_SIZE);
        userEntity.setPictureName(DEFAULT_AVATAR_PATH);

        userRepository.save(userEntity);
    }

    void createAndSendToken(UserEntity userEntity) {

        String token = tokenGenerator.generateConfirmationToken(userEntity.getId());

        EmailVerifyTokenEntity confirmationToken = new EmailVerifyTokenEntity(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userEntity
        );

        confirmationTokenRepository.save(confirmationToken);

        String confirmationLink = emailVerificationLink + token;

        Map<String, Object> model = new HashMap<>();
        model.put("firstName", userEntity.getFirstName());
        model.put("confirmationLink", confirmationLink);

        emailService.sendEmail(userEntity.getEmail(), model, "email-confirmation.html", "Confirm your email");
    }
}
