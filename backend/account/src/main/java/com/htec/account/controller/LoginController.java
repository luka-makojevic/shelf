package com.htec.account.controller;

import com.htec.account.dto.UserDTO;
import com.htec.account.mapper.UserMapper;
import com.htec.account.model.request.UserLoginMicrosoftRequestModel;
import com.htec.account.model.request.UserLoginRequestModel;
import com.htec.account.model.response.UserLoginResponseModel;
import com.htec.account.service.LoginService;
import com.htec.account.util.TokenGenerator;
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

        String jwtToken = tokenGenerator.generateJwtToken(loggedInUser);

        String jwtRefreshToken = tokenGenerator.generateJwtRefreshToken(loggedInUser);

        UserLoginResponseModel userLoginResponse = UserMapper.INSTANCE.userDtoToUserLoginResponseModel(loggedInUser, jwtToken, jwtRefreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(userLoginResponse);
    }

    @PostMapping(value = "/microsoft", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseModel> loginUserMicrosoft(@RequestBody UserLoginMicrosoftRequestModel userLoginRequestModel) {

        UserDTO loggedInUser = loginService.authenticateUserMicrosoft(userLoginRequestModel.getBearerToken());

        String jwtToken = tokenGenerator.generateJwtToken(loggedInUser);

        String jwtRefreshToken = tokenGenerator.generateJwtRefreshToken(loggedInUser);

        UserLoginResponseModel userLoginResponse = UserMapper.INSTANCE.userDtoToUserLoginResponseModel(loggedInUser, jwtToken, jwtRefreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(userLoginResponse);
    }
}
