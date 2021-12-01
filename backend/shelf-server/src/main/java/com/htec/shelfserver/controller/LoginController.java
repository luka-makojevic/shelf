package com.htec.shelfserver.controller;

import com.htec.shelfserver.annotation.AuthenticationUser;
import com.htec.shelfserver.dto.AuthUser;
import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.model.request.UserLoginMicrosoftRequestModel;
import com.htec.shelfserver.model.request.UserLoginRequestModel;
import com.htec.shelfserver.model.request.UserLogoutRequestModel;
import com.htec.shelfserver.model.response.TextResponseMessage;
import com.htec.shelfserver.model.response.UserLoginResponseModel;
import com.htec.shelfserver.service.LoginService;
import com.htec.shelfserver.util.TokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ResponseEntity<UserLoginResponseModel> createUser(@RequestBody UserLoginMicrosoftRequestModel userLoginRequestModel) {

        UserDTO loggedInUser = loginService.authenticateUserMicrosoft(userLoginRequestModel.getBearerToken());

        String jwtToken = tokenGenerator.generateJwtToken(loggedInUser);

        String jwtRefreshToken = tokenGenerator.generateJwtRefreshToken(loggedInUser);

        UserLoginResponseModel userLoginResponse = UserMapper.INSTANCE.userDtoToUserLoginResponseModel(loggedInUser, jwtToken, jwtRefreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(userLoginResponse);
    }
}
