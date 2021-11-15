package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.ConfirmationTokenEntity;
import com.htec.shelfserver.entity.RoleEntity;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.repository.ConfirmationTokenRepository;
import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.util.ErrorMessages;
import com.htec.shelfserver.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    final private UserRepository userRepository;
    final private ConfirmationTokenRepository confirmationTokenRepository;
    final private Utils utils;
    final private BCryptPasswordEncoder bCryptPasswordEncoder;
    final private  EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository,
                       ConfirmationTokenRepository confirmationTokenRepository,
                       Utils utils,
                       BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
    }

    public UserDTO createUser(UserDTO userDTO) throws Exception {

        if (userDTO.getEmail() == null || userDTO.getPassword() == null || userDTO.getFirstName() == null || userDTO.getLastName() == null)
            throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        if (userRepository.findByEmail(userDTO.getEmail()) != null)
            throw new Exception(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());

        UserEntity userEntity = UserMapper.INSTANCE.userDtoToUser(userDTO);

        userEntity.setCreatedAt(new Date());
        userEntity.setEmailVerified(false);
        userEntity.setRole(new RoleEntity(3L));

        String salt = utils.generateSalt(8);
        userEntity.setSalt(salt);

        String encryptedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword() + salt);
        userEntity.setPassword(encryptedPassword);

        UserEntity storedUser = userRepository.save(userEntity);

        String token = UUID.randomUUID().toString();

        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(
                token,
                new Date(),
                Date.from((new Date()).toInstant().plusSeconds(60 * 15)),
                storedUser
        );

        confirmationTokenRepository.save(confirmationToken);

        String link = "http://10.10.0.120:8080/users/register/confirm?token=" + token;
        emailService.send(storedUser.getEmail() , emailService.buildEmail(storedUser.getFirstName() , link));

        return UserMapper.INSTANCE.userToUserDTO(storedUser);

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
    }
}
