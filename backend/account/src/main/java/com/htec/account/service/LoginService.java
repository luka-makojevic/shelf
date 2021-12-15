package com.htec.account.service;

import com.htec.account.dto.UserDTO;
import com.htec.account.entity.RoleEntity;
import com.htec.account.entity.UserEntity;
import com.htec.account.exception.ExceptionSupplier;
import com.htec.account.mapper.UserMapper;
import com.htec.account.model.response.UserRegisterMicrosoftResponseModel;
import com.htec.account.repository.mysql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private final MicrosoftApiService microsoftApiService;

    private final Long FREE_SPACE_SIZE;

    @Autowired
    LoginService(UserRepository userRepository,
                 AuthenticationManager authenticationManager,
                 MicrosoftApiService microsoftApiService,
                 @Value("${userFreeSpace}") Long free_space_size) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.microsoftApiService = microsoftApiService;
        this.FREE_SPACE_SIZE = free_space_size;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email).
                orElseThrow(ExceptionSupplier.recordNotFoundWithEmail);

        return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
    }

    public UserDTO authenticateUser(UserDTO userDTO) {

        UserEntity userEntity = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(ExceptionSupplier.userNotFound);

        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(userDTO.getEmail(),
                userDTO.getPassword() + userEntity.getSalt());

        Authentication auth;
        try {
            auth = authenticationManager.authenticate(authReq);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
        } catch (AuthenticationException ex) {
            throw ExceptionSupplier.authenticationCredentialsNotValid.get();
        }

        if (!userEntity.getEmailVerified())
            throw ExceptionSupplier.emailNotVerified.get();

        return UserMapper.INSTANCE.userEntityToUserDTO(userEntity);
    }

    public UserDTO authenticateUserMicrosoft(String bearerToken) {

        UserRegisterMicrosoftResponseModel response = microsoftApiService.getUserInfo(bearerToken)
                .orElseThrow(ExceptionSupplier.accessTokenNotActive);

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(response.getMail());

        UserEntity userEntity;

        if (!userEntityOptional.isPresent()) {

            userEntity = UserMapper.INSTANCE.userRegisterMicrosoftResponseModelToUserEntity(response);

            userEntity.setCreatedAt(LocalDateTime.now());
            userEntity.setEmailVerified(true);
            userEntity.setRole(new RoleEntity(3L));
            userEntity.setFreeSpace(FREE_SPACE_SIZE);

            userRepository.save(userEntity);

        } else {
            userEntity = userEntityOptional.get();
        }

        return UserMapper.INSTANCE.userEntityToUserDTO(userEntity);
    }
}
