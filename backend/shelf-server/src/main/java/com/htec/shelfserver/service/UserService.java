package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.PasswordResetTokenEntity;
import com.htec.shelfserver.entity.RoleEntity;
import com.htec.shelfserver.entity.TokenEntity;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.model.response.UserResponseModel;
import com.htec.shelfserver.repository.PasswordResetTokenRepository;
import com.htec.shelfserver.repository.TokenRepository;
import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.util.Roles;
import com.htec.shelfserver.util.TokenGenerator;
import com.htec.shelfserver.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository confirmationTokenRepository;
    private final TokenGenerator tokenGenerator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final UserValidator userValidator;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final String emailVerificationLink;

    private final String emailPasswordResetTokenLink;

    @Autowired
    public UserService(PasswordResetTokenRepository passwordResetTokenRepository,
                       UserRepository userRepository,
                       TokenRepository confirmationTokenRepository,
                       TokenGenerator tokenGenerator,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       EmailService emailService,
                       UserValidator userValidator,
                       @Value("${emailVerificationLink}") String emailVerificationLink,
                       @Value("${emailPasswordResetTokenLink}") String emailPasswordResetTokenLink) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.tokenGenerator = tokenGenerator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.userValidator = userValidator;
        this.emailVerificationLink = emailVerificationLink;
        this.emailPasswordResetTokenLink = emailPasswordResetTokenLink;
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

    void createAndSendToken(UserEntity userEntity) {
        String token = tokenGenerator.generateConfirmationToken(userEntity.getId());

        TokenEntity confirmationToken = new TokenEntity(
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

    public void registerUserMicrosoft(String bearerToken) {
        //todo: implement
    }

    public UserDTO getUser(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).
                orElseThrow(ExceptionSupplier.recordNotFoundWithEmail);

        return UserMapper.INSTANCE.userEntityToUserDTO(userEntity);
    }

    public List<UserResponseModel> getUsers() {
        return UserMapper.INSTANCE.userEntityToUserResponseModels(userRepository.findAll());
    }

    public UserResponseModel getUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(ExceptionSupplier.recordNotFoundWithId);

        return UserMapper.INSTANCE.userEntityToUserResponseModel(user);
    }

    public void deleteUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(ExceptionSupplier.recordNotFoundWithId);

        if (user.getRole() != null) {
            if (user.getRole().getId().equals(Long.valueOf(Roles.SUPER_ADMIN))) {
                userRepository.delete(user);
            }
        } else {
            throw ExceptionSupplier.userNotValid.get();
        }
    }

    public void requestPasswordReset(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(ExceptionSupplier.userNotValid);

        sendPasswordResetMail(userEntity);
    }

    void sendPasswordResetMail(UserEntity userEntity) {
        String token = tokenGenerator.generatePasswordResetToken(userEntity.getId().toString());

        PasswordResetTokenEntity passwordResetToken = new PasswordResetTokenEntity(token, userEntity);

        passwordResetTokenRepository.save(passwordResetToken);

        String passwordResetTokenLink = emailPasswordResetTokenLink + token;

        Map<String, Object> model = new HashMap<>();
        model.put("firstName", userEntity.getFirstName());
        model.put("passwordResetTokenLink", passwordResetTokenLink);

        emailService.sendEmail(userEntity.getEmail(), model, "password-reset.html", "Reset your password");
    }

}
