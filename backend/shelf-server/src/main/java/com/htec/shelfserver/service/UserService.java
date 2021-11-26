package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.model.response.UserResponseModel;
import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.util.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    @Override
    public UserDetails loadUserByUsername(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).
                orElseThrow(ExceptionSupplier.recordNotFoundWithEmail);

        return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
    }

}
