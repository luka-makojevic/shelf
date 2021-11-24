package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.RoleEntity;
import com.htec.shelfserver.entity.TokenEntity;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.enumes.Roles;
import com.htec.shelfserver.exceptionSupplier.ExceptionSupplier;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.repository.TokenRepository;
import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.responseModel.UserResponseModel;
import com.htec.shelfserver.util.UserValidator;
import com.htec.shelfserver.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenRepository confirmationTokenRepository;
    private final Utils utils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final UserValidator userValidator;

    private final String emailVerificationLink;
    private final String emailResendTokenLink;

    @Autowired
    public UserService(UserRepository userRepository,
                       TokenRepository confirmationTokenRepository,
                       Utils utils,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       EmailService emailService,
                       UserValidator userValidator,
                       @Value("${emailVerificationLink}") String emailVerificationLink,
                       @Value("${emailResendTokenLink}")String emailResendTokenLink) {
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.userValidator = userValidator;
        this.emailVerificationLink = emailVerificationLink;
        this.emailResendTokenLink = emailResendTokenLink;
    }

    public void createUser(UserDTO userDTO) {

        userRepository.findByEmail(userDTO.getEmail()).ifPresent(
                userEntity -> {
                    throw ExceptionSupplier.recordAlreadyExists.get();
                });

        userValidator.isUserValid(userDTO);

        UserEntity userEntity = UserMapper.INSTANCE.userDtoToUserEntity(userDTO);

        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setEmailVerified(false);
        userEntity.setRole(new RoleEntity(3L));

        String salt = utils.generateSalt(8);
        userEntity.setSalt(salt);

        String encryptedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword() + salt);
        userEntity.setPassword(encryptedPassword);

        UserEntity storedUser = userRepository.save(userEntity);
        createAndSendToken(storedUser);

    }

    void createAndSendToken(UserEntity userEntity) {
        String token = utils.generateConfirmationToken(userEntity.getId());

        TokenEntity confirmationToken = new TokenEntity(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userEntity
        );

        confirmationTokenRepository.save(confirmationToken);

        String confirmationLink = emailVerificationLink + token;
        String resendTokenLink = emailResendTokenLink + token;

        Map<String, Object> model = new HashMap<>();
        model.put("firstName", userEntity.getFirstName());
        model.put("confirmationLink", confirmationLink);
        model.put("resendTokenLink", resendTokenLink);

        emailService.sendEmail(userEntity.getEmail(), model, "email-confirmation.html", "Confirm your email");
    }

    public UserDTO getUser(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(
                ExceptionSupplier.recordNotFoundWithEmail
        );

        return UserMapper.INSTANCE.userEntityToUserDTO(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).
                orElseThrow(ExceptionSupplier.recordNotFoundWithEmail);

        return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
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
            ExceptionSupplier.userNotValid.get();
        }

    }
}
