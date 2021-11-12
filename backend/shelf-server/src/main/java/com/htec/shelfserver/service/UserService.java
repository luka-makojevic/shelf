package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.mapper.UserMapper;
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

@Service
public class UserService implements UserDetailsService {

    final private UserRepository userRepository;
    final private Utils utils;
    final private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, Utils utils, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserDTO createUser(UserDTO userDTO) throws Exception{

        if (userRepository.findByEmail(userDTO.getEmail()) != null)
            throw new Exception(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());

        UserEntity userEntity = UserMapper.INSTANCE.userDtoToUser(userDTO);

        String salt = utils.generateSalt(8);
        userEntity.setSalt(salt);

        String encryptedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword() + salt);
        userEntity.setPassword(encryptedPassword);

        UserEntity storedUser = userRepository.save(userEntity);

        return UserMapper.INSTANCE.userToUserDTO(storedUser);

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity =  userRepository.findByEmail(email);

        if(userEntity == null)
            throw new UsernameNotFoundException(email);
        
        return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
    }
}
