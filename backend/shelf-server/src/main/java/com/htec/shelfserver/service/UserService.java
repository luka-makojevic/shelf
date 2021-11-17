package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.ConfirmationTokenEntity;
import com.htec.shelfserver.entity.RoleEntity;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ShelfException;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.repository.ConfirmationTokenRepository;
import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.responseModel.ResponseMessage;
import com.htec.shelfserver.util.ErrorMessages;
import com.htec.shelfserver.util.UserValidator;
import com.htec.shelfserver.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final Utils utils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final UserValidator userValidator;

    private final String serverIp;

    @Autowired
    public UserService(UserRepository userRepository,
                       ConfirmationTokenRepository confirmationTokenRepository,
                       Utils utils,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       EmailService emailService,
                       UserValidator userValidator, @Value("${shelfserver}") String serverIp) {
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.userValidator = userValidator;
        this.serverIp = serverIp;
    }

    public ResponseMessage createUser(UserDTO userDTO) {

        userRepository.findByEmail(userDTO.getEmail()).ifPresent(
                userEntity -> {
                    throw new ShelfException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage(),
                            HttpStatus.BAD_REQUEST.value(),
                            "", ErrorMessages.BAD_REQUEST.getErrorMessage());
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = LocalDateTime.now().format(formatter);

        return new ResponseMessage("User registered.", HttpStatus.CREATED.value(), formatDateTime, "Created user.");
    }

    private void createAndSendToken(UserEntity userEntity) {
        String token = UUID.randomUUID().toString();

        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userEntity
        );

        confirmationTokenRepository.save(confirmationToken);

        String link = "http://" + serverIp + "/users/register/confirmation?token=" + token;

        Map<String, Object> model = new HashMap<>();
        model.put("firstName", userEntity.getFirstName());
        model.put("confirmationLink", link);

        emailService.sendEmail(userEntity.getEmail(), model);
    }

    public UserDTO getUser(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(
                () -> new ShelfException(ErrorMessages.NO_RECORD_FOUND_WITH_EMAIL.getErrorMessage() + email,
                        HttpStatus.NOT_FOUND.value(),
                        "", ErrorMessages.NOT_FOUND.getErrorMessage())
        );

        return UserMapper.INSTANCE.userEntityToUserDTO(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(
                () -> new ShelfException(ErrorMessages.NO_RECORD_FOUND_WITH_EMAIL.getErrorMessage() + email,
                        HttpStatus.NOT_FOUND.value(),
                        "", ErrorMessages.NOT_FOUND.getErrorMessage())
        );

        return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
    }
}
