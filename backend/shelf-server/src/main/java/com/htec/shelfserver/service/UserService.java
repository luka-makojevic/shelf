package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.User;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final private UserRepository userRepository;
    final private Utils utils;
    final private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, Utils utils, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserDTO createUser(UserDTO userDTO) {

        if (userRepository.findByEmail("userEmail") != null)
            throw new RuntimeException("Record already exists!");

        User userEntity = UserMapper.INSTANCE.userDtoToUser(userDTO);

        String salt = utils.generateSalt(8);
        userEntity.setSalt(salt);

        String encryptedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword() + salt);
        userEntity.setPassword(encryptedPassword);

        User storedUser = userRepository.save(userEntity);

        UserDTO returnValue = UserMapper.INSTANCE.userToUserDTO(storedUser);

        return returnValue;

    }
}
