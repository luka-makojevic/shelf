package com.htec.shelfserver.service;


import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.PasswordResetTokenEntity;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.model.response.UserResponseModel;
import com.htec.shelfserver.repository.PasswordResetTokenRepository;
import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.util.Roles;
import com.htec.shelfserver.util.TokenGenerator;
<<<<<<< Updated upstream
=======
import com.htec.shelfserver.validator.UserValidator;
>>>>>>> Stashed changes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final TokenGenerator tokenGenerator;
    private final EmailService emailService;
    private final String emailPasswordResetTokenLink;

    @Autowired
    public UserService(PasswordResetTokenRepository passwordResetTokenRepository,
                       UserRepository userRepository,
                       TokenGenerator tokenGenerator,
                       EmailService emailService,
                       @Value("${emailPasswordResetTokenLink}") String emailPasswordResetTokenLink) {

        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.emailService = emailService;
        this.emailPasswordResetTokenLink = emailPasswordResetTokenLink;
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

<<<<<<< Updated upstream
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

=======
    public void updateUser(UserDTO userDTO) {
        UserEntity user = userRepository.findById(userDTO.getId()).orElseThrow(ExceptionSupplier.recordNotFoundWithId);

        userValidator.isUserUpdateValid(userDTO);

        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        }

        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }

        if (userDTO.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(user);
    }
>>>>>>> Stashed changes
}
