package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.mapper.UserMapper;
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
import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    LoginService(UserRepository userRepository, AuthenticationManager authenticationManager) {

        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email).
                orElseThrow(ExceptionSupplier.recordNotFoundWithEmail);

        return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
    }

    public UserDTO authenticateUser(UserDTO userDTO) {
        UserDTO returnValue = new UserDTO();
        Optional<UserEntity> userEntity = userRepository.findByEmail(userDTO.getEmail());

        String salt = userEntity.isPresent() ? userEntity.get().getSalt() : "";

        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(userDTO.getEmail(),
                userDTO.getPassword() + salt);
        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(authReq);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
        } catch (AuthenticationException ex) {
            throw ExceptionSupplier.userNotValid.get();
        }

        if (!userEntity.get().getEmailVerified())
            throw ExceptionSupplier.emailNotVerified.get();

        returnValue = UserMapper.INSTANCE.userEntityToUserDTO(userEntity.get());
        return returnValue;
    }


}
