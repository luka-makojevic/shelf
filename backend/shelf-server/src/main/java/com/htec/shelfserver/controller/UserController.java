package com.htec.shelfserver.controller;

import com.htec.shelfserver.annotation.AuthenticationUser;
import com.htec.shelfserver.dto.AuthUser;
import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.model.request.UserRegisterMicrosoftRequestModel;
import com.htec.shelfserver.model.request.UserRegisterRequestModel;
import com.htec.shelfserver.model.response.TextResponseMessage;
import com.htec.shelfserver.requestModel.PasswordResetRequestModel;
import com.htec.shelfserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    final private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@AuthenticationUser AuthUser user, @PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextResponseMessage> registerUser(@RequestBody UserRegisterRequestModel userRegisterRequestModel) {

        UserDTO userDTO = UserMapper.INSTANCE.userRegisterRequestModelToUserDto(userRegisterRequestModel);

        userService.registerUser(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new TextResponseMessage("User registered", HttpStatus.CREATED.value()));
    }

    @PostMapping(value = "/register/microsoft", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextResponseMessage> registerUserMicrosoft(@RequestBody UserRegisterMicrosoftRequestModel userRegisterMicrosoftRequestModel) {

        userService.registerUserMicrosoft(userRegisterMicrosoftRequestModel.getBearerToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(new TextResponseMessage("User registered", HttpStatus.CREATED.value()));
    }

    @PutMapping
    public String updateUser() {
        return "Update user is called.";
    }

    @PostMapping(value = "/password-reset-request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {

        userService.requestPasswordReset(passwordResetRequestModel.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("Email sent" , HttpStatus.OK.value()));
    }
}
