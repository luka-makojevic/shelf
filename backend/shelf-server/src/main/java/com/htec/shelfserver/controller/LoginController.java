package com.htec.shelfserver.controller;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.model.request.UserLoginRequestModel;
import com.htec.shelfserver.model.response.UserLoginResponseModel;
import com.htec.shelfserver.service.LoginService;
import com.htec.shelfserver.util.TokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;
    private final TokenGenerator tokenGenerator;

    public LoginController(LoginService loginService, TokenGenerator tokenGenerator) {

        this.loginService = loginService;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseModel> loginUser(@RequestBody UserLoginRequestModel userLoginRequestModel) {

        UserDTO userDTO = UserMapper.INSTANCE.userLoginRequestModelToUserDto(userLoginRequestModel);

        UserDTO loggedInUser = loginService.authenticateUser(userDTO);

        String jwtToken = tokenGenerator.generateJwtToken(userDTO);

        String jwtRefreshToken = tokenGenerator.generateJwtRefreshToken(userDTO);

        UserLoginResponseModel userLoginResponse = UserMapper.INSTANCE.userDtoToUserLoginResponseModel(userDTO, jwtToken, jwtRefreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(userLoginResponse);
    }
}
