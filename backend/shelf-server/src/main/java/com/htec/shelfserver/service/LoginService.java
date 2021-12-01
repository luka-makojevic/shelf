package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.model.response.UserRegisterMicrosoftResponseModel;
import com.htec.shelfserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;

@Service
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private final MicrosoftApiService microsoftApiService;

    @Autowired
    LoginService(UserRepository userRepository,
                 AuthenticationManager authenticationManager,
                 MicrosoftApiService microsoftApiService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.microsoftApiService = microsoftApiService;
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

        UserEntity userEntity = userRepository.findByEmail(response.getMail())
                .orElseThrow(ExceptionSupplier.userNotFound);

        if (!userEntity.getEmailVerified())
            throw ExceptionSupplier.emailNotVerified.get();

        return UserMapper.INSTANCE.userEntityToUserDTO(userEntity);
    }
}
