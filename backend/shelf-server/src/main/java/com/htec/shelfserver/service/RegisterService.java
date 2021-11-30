package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.RoleEntity;
import com.htec.shelfserver.entity.EmailVerifyTokenEntity;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.model.response.UserRegisterMicrosoftResponseModel;
import com.htec.shelfserver.repository.TokenRepository;
import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.security.SecurityConstants;
import com.htec.shelfserver.util.TokenGenerator;
import com.htec.shelfserver.validator.UserValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final String MICROSOFT_GRAPH_URL = "https://graph.microsoft.com/v1.0/me";

    private final TokenRepository confirmationTokenRepository;
    private final TokenGenerator tokenGenerator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final UserValidator userValidator;

    private final String emailVerificationLink;

    public RegisterService(UserRepository userRepository,
                           TokenRepository confirmationTokenRepository,
                           TokenGenerator tokenGenerator, BCryptPasswordEncoder bCryptPasswordEncoder,
                           EmailService emailService, UserValidator userValidator,
                           @Value("${emailVerificationLink}") String emailVerificationLink) {

        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.tokenGenerator = tokenGenerator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.userValidator = userValidator;
        this.emailVerificationLink = emailVerificationLink;

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

        String salt = tokenGenerator.generateSalt(8);
        userEntity.setSalt(salt);

        String encryptedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword() + salt);
        userEntity.setPassword(encryptedPassword);

        UserEntity storedUser = userRepository.save(userEntity);
        createAndSendToken(storedUser);
    }

    public void registerUserMicrosoft(String bearerToken) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.AUTHORIZATION_HEADER_STRING, SecurityConstants.BEARER_TOKEN_PREFIX + bearerToken);

        ResponseEntity<UserRegisterMicrosoftResponseModel> response;

        try {
            response = restTemplate.exchange(MICROSOFT_GRAPH_URL,
                    HttpMethod.GET, new HttpEntity<>(headers),
                    UserRegisterMicrosoftResponseModel.class);

        } catch (RestClientException ex) {
            throw ExceptionSupplier.accessTokenNotActive.get();
        }

        userRepository.findByEmail(response.getBody().getMail()).ifPresent(
                userEntity -> {
                    throw ExceptionSupplier.recordAlreadyExists.get();
                });

        UserEntity userEntity = UserMapper.INSTANCE.userRegisterMicrosoftResponseModelToUserEntity(response.getBody());

        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setEmailVerified(true);
        userEntity.setRole(new RoleEntity(3L));

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
