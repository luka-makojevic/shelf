package com.htec.shelfserver.controller;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.requestModel.UserRequestModel;
import com.htec.shelfserver.responseModel.UserResponseModel;
import com.htec.shelfserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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

    @PostMapping(value="/register" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody UserRequestModel userRequestModel , Model model) throws Exception{

        UserDTO userDTO = UserMapper.INSTANCE.userRequestModelToUserDto(userRequestModel);

        return userService.createUser(userDTO);

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
