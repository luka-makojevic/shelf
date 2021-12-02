package com.htec.shelfserver.service;


import com.htec.shelfserver.dto.AuthUser;
import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.PasswordResetTokenEntity;
import com.htec.shelfserver.entity.RoleEntity;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.model.request.PasswordResetModel;
import com.htec.shelfserver.model.response.UserPageResponseModel;
import com.htec.shelfserver.model.response.UserResponseModel;
import com.htec.shelfserver.repository.mysql.RoleRepository;
import com.htec.shelfserver.repository.mysql.PasswordResetTokenRepository;
import com.htec.shelfserver.repository.mysql.UserRepository;
import com.htec.shelfserver.security.SecurityConstants;
import com.htec.shelfserver.util.Roles;
import com.htec.shelfserver.util.TokenGenerator;
import com.htec.shelfserver.validator.UserValidator;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final UserValidator userValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final String emailPasswordResetTokenLink;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(PasswordResetTokenRepository passwordResetTokenRepository,
                       UserRepository userRepository,
                       TokenGenerator tokenGenerator,
                       EmailService emailService,
                       UserValidator userValidator,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       @Value("${emailPasswordResetTokenLink}") String emailPasswordResetTokenLink,
                       RoleRepository roleRepository) {

        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.emailService = emailService;
        this.userValidator = userValidator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailPasswordResetTokenLink = emailPasswordResetTokenLink;
        this.roleRepository = roleRepository;
    }


    public UserDTO getUser(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).
                orElseThrow(ExceptionSupplier.recordNotFoundWithEmail);

        return UserMapper.INSTANCE.userEntityToUserDTO(userEntity);
    }

    public UserPageResponseModel<UserResponseModel> getUsers(AuthUser user, Integer page, Integer size) {

        if (page <= 0) {
            throw ExceptionSupplier.pageWrong.get();
        }

        if (size < 1) {
            throw ExceptionSupplier.sizeWrong.get();
        }

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<UserEntity> users = userRepository.findAll(user.getId(), pageable);

        List<UserResponseModel> allUsers = UserMapper.INSTANCE.userEntityToUserResponseModels(users.getContent());

        return new UserPageResponseModel<>(allUsers, users.getTotalPages(), users.getNumber() + 1, allUsers.size());
    }

    public UserResponseModel getUserById(Long id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(ExceptionSupplier.recordNotFoundWithId);

        return UserMapper.INSTANCE.userEntityToUserResponseModel(user);
    }

    public void deleteUserById(Long id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(ExceptionSupplier.recordNotFoundWithId);

        if (user.getRole() != null) {
            if (!user.getRole().getId().equals(Long.valueOf(Roles.SUPER_ADMIN))) {
                userRepository.delete(user);
            }
        } else {
            throw ExceptionSupplier.userDoesNotHavePermission.get();
        }
    }

    public void requestPasswordReset(String email) {

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(ExceptionSupplier.recordNotFoundWithEmail);

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

    public UserResponseModel updateUser(UserDTO userDTO) {

        UserEntity user = userRepository.findById(userDTO.getId())
                .orElseThrow(ExceptionSupplier.recordNotFoundWithId);

        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        } else {
            user.setFirstName(user.getFirstName());
        }

        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        } else {
            user.setLastName(user.getLastName());
        }

        if (userDTO.getPassword() != null) {
            userValidator.isUserPasswordValid(userDTO.getPassword());
            user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword() + user.getSalt()));
        } else {
            user.setPassword(user.getPassword());
        }

        userRepository.save(user);

        return UserMapper.INSTANCE.userEntityToUserResponseModel(user);
    }

    public UserResponseModel updateUserRole(Long id, Long roleId) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(ExceptionSupplier.recordNotFoundWithId);

        if (user.getRole().getId().equals(roleId)) {
            throw ExceptionSupplier.wrongRoleUpdate.get();
        }

        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(ExceptionSupplier.recordNotFoundWithId);

        user.setRole(new RoleEntity(role.getId(), role.getName()));

        userRepository.save(user);

        Map<String, Object> model = new HashMap<>();
        model.put("firstName", user.getFirstName());
        model.put("roleName", user.getRole().getName());

        emailService.sendEmail(user.getEmail(), model, "update-role-email.html", "Role updated");

        return UserMapper.INSTANCE.userEntityToUserResponseModel(user);
    }

    public void resetPassword(PasswordResetModel passwordResetModel) {

        String password = passwordResetModel.getPassword();
        userValidator.isUserPasswordValid(password);
        String jwtToken = passwordResetModel.getJwtToken();

        if(!checkUserByJwtToken(jwtToken))
            throw ExceptionSupplier.tokenNotValid.get();

        if (!passwordResetTokenRepository.findByToken(jwtToken).isPresent())
            throw ExceptionSupplier.emailResetRequestWasNotSent.get();

        long userId = findUserIdByJwtToken(jwtToken);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(ExceptionSupplier.recordNotFoundWithEmail);

        String salt = userEntity.getSalt();
        String newPassword = bCryptPasswordEncoder.encode(password + salt);
        userEntity.setPassword(newPassword);
        userRepository.save(userEntity);

        passwordResetTokenRepository.deleteAllByUserDetails(userEntity);
    }

    public boolean checkUserByJwtToken(String jwtToken) {

        String user;
        try {
            user = Jwts.parser()
                    .setSigningKey(SecurityConstants.TOKEN_SECRET)
                    .parseClaimsJws(jwtToken)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw ExceptionSupplier.tokenExpired.get();
        } catch (Exception e) {
            throw ExceptionSupplier.tokenNotValid.get();
        }

        return user != null;
    }

    public long findUserIdByJwtToken(String jwtToken) {
        return passwordResetTokenRepository.findByToken(jwtToken)
                .orElseThrow(ExceptionSupplier.emailResetRequestWasNotSent)
                .getUserDetails().getId();
    }
}
