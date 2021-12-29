package com.htec.account.service;

import com.htec.account.annotation.Roles;
import com.htec.account.dto.AuthUser;
import com.htec.account.dto.UserDTO;
import com.htec.account.entity.PasswordResetTokenEntity;
import com.htec.account.entity.RoleEntity;
import com.htec.account.entity.UserEntity;
import com.htec.account.exception.ExceptionSupplier;
import com.htec.account.exception.ShelfException;
import com.htec.account.mapper.UserMapper;
import com.htec.account.model.request.PasswordResetModel;
import com.htec.account.model.request.UpdateUserPhotoByIdRequestModel;
import com.htec.account.model.response.RefreshTokenResponseModel;
import com.htec.account.model.response.UserPageResponseModel;
import com.htec.account.model.response.UserResponseModel;
import com.htec.account.repository.cassandra.InvalidJwtTokenRepository;
import com.htec.account.repository.mysql.PasswordResetTokenRepository;
import com.htec.account.repository.mysql.RoleRepository;
import com.htec.account.repository.mysql.UserRepository;
import com.htec.account.security.SecurityConstants;
import com.htec.account.util.TokenGenerator;
import com.htec.account.validator.UserValidator;
import io.jsonwebtoken.Claims;
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
    private final InvalidJwtTokenRepository invalidJwtTokenRepository;

    @Autowired
    public UserService(PasswordResetTokenRepository passwordResetTokenRepository,
                       UserRepository userRepository,
                       TokenGenerator tokenGenerator,
                       EmailService emailService,
                       UserValidator userValidator,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       @Value("${emailPasswordResetTokenLink}") String emailPasswordResetTokenLink,
                       RoleRepository roleRepository,
                       InvalidJwtTokenRepository invalidJwtTokenRepository) {

        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.emailService = emailService;
        this.userValidator = userValidator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailPasswordResetTokenLink = emailPasswordResetTokenLink;
        this.roleRepository = roleRepository;
        this.invalidJwtTokenRepository = invalidJwtTokenRepository;
    }

    public UserDTO getUser(String email) {

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(ExceptionSupplier.recordNotFoundWithEmail);

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

        UserEntity user = userRepository.getUserByIdAndRoleIdNot(id, 1L)
                .orElseThrow(ExceptionSupplier.recordNotFoundWithId);

        return UserMapper.INSTANCE.userEntityToUserResponseModel(user);
    }

    public void deleteUserById(Long id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(ExceptionSupplier.recordNotFoundWithId);

        if (user.getRole() != null) {
            if (!user.getRole().getId().equals(Roles.SUPER_ADMIN.getValue())) {
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

    public UserResponseModel updateUserProfilePicture(UpdateUserPhotoByIdRequestModel updateUserPhotoByIdRequestModel) {

        UserEntity user = userRepository.findById(updateUserPhotoByIdRequestModel.getId())
                .orElseThrow(ExceptionSupplier.recordNotFoundWithId);

        if (updateUserPhotoByIdRequestModel.getPictureName() != null)
            user.setPictureName(updateUserPhotoByIdRequestModel.getPictureName());

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

        if (!checkUserByJwtToken(jwtToken)) throw ExceptionSupplier.tokenNotValid.get();

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
            user = Jwts
                    .parser()
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
                .orElseThrow(ExceptionSupplier.emailResetRequestWasNotSent).getUserDetails().getId();
    }

    public String getPicturePathByUserId(Long userId) {

        return userRepository.getPicturePathByUserId(userId)
                .orElseThrow(ExceptionSupplier.userNotFound);
    }

    public RefreshTokenResponseModel sendNewAccessToken(String refreshToken) {

        AuthUser authUser;
        try {
            authUser = validateRefreshToken(refreshToken);
        } catch (ShelfException e) {

            throw ExceptionSupplier.userIsNotLoggedIn.get();
        } catch (ExpiredJwtException e) {

            throw ExceptionSupplier.refreshTokenExpired.get();
        } catch (Exception e) {

            throw ExceptionSupplier.refreshTokenNotValid.get();
        }

        if (authUser == null) {
            throw ExceptionSupplier.refreshTokenNotValid.get();
        }
        UserDTO userDTO = UserMapper.INSTANCE.authUserToUserDTO(authUser);
        String jwtToken = tokenGenerator.generateJwtToken(userDTO);

        return new RefreshTokenResponseModel(jwtToken);
    }

    public AuthUser validateRefreshToken(String refreshToken) {

        if (refreshToken != null) {

            if (invalidJwtTokenRepository.findByJwt(refreshToken).isPresent()) {
                throw new ShelfException();
            }

            String email;

            Claims body = Jwts.parser()
                    .setSigningKey(SecurityConstants.TOKEN_SECRET)
                    .parseClaimsJws(refreshToken)
                    .getBody();

            email = body
                    .getSubject();

            if (email.isEmpty()) {
                throw ExceptionSupplier.refreshTokenNotValid.get();
            }

            userRepository.findByEmail(email)
                    .orElseThrow(ExceptionSupplier.refreshTokenNotValid);

            Long userId = Long.valueOf(body.getId());
            Long roleId = body.get("role_id", Long.class);

            return new AuthUser(userId, email, roleId);
        } else {
            throw ExceptionSupplier.tokenNotFound.get();
        }
    }

}
