package com.htec.shelfserver.controller;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.model.request.UserRegisterMicrosoftRequestModel;
import com.htec.shelfserver.model.request.UserRegisterRequestModel;
import com.htec.shelfserver.model.response.TextResponseMessage;
import com.htec.shelfserver.service.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {

    final private RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextResponseMessage> registerUser(@RequestBody UserRegisterRequestModel userRegisterRequestModel) {

        UserDTO userDTO = UserMapper.INSTANCE.userRegisterRequestModelToUserDto(userRegisterRequestModel);

        registerService.registerUser(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new TextResponseMessage("User registered", HttpStatus.CREATED.value()));
    }

    @PostMapping(value = "/microsoft", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TextResponseMessage> registerUserMicrosoft(@RequestBody UserRegisterMicrosoftRequestModel userRegisterMicrosoftRequestModel) {

        registerService.registerUserMicrosoft(userRegisterMicrosoftRequestModel.getBearerToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(new TextResponseMessage("User registered", HttpStatus.CREATED.value()));
    }
}
