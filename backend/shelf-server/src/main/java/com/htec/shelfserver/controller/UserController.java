package com.htec.shelfserver.controller;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.requestModel.UserRequestModel;
import com.htec.shelfserver.responseModel.UserResponseModel;
import com.htec.shelfserver.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    final private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUsers() {
        return "Get users is called.";
    }

    @PostMapping
    public ResponseEntity<UserResponseModel> createUser(@RequestBody UserRequestModel userRequestModel) {

        UserDTO userDTO = UserMapper.INSTANCE.userRequestModelToUserDto(userRequestModel);

        UserDTO createdUser = userService.createUser(userDTO);

        UserResponseModel response = UserMapper.INSTANCE.userDtoToUserResponseModel(createdUser);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PutMapping
    public String updateUser() {
        return "Update user is called.";
    }

    @DeleteMapping
    public String deleteUser() {
        return "Delete user is called.";
    }
}
