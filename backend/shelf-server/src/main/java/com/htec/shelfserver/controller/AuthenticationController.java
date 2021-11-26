package com.htec.shelfserver.controller;

import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.model.request.UserLoginRequestModel;
import com.htec.shelfserver.model.response.UserLoginResponseModel;
import com.htec.shelfserver.service.AuthenticationService;
import com.htec.shelfserver.util.TokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TokenGenerator tokenGenerator;

    public AuthenticationController(AuthenticationService authenticationService, TokenGenerator tokenGenerator) {

        this.authenticationService = authenticationService;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseModel> createUser(@RequestBody UserLoginRequestModel userLoginRequestModel) {

        UserDTO userDTO = UserMapper.INSTANCE.userLoginRequestModelToUserDto(userLoginRequestModel);

        UserDTO loggedInUser = authenticationService.authenticateUser(userDTO);
        String jwtToken =  tokenGenerator.generateJwtToken(userDTO);

        return ResponseEntity.status(HttpStatus.OK).body(new UserLoginResponseModel(loggedInUser.getId(),
                loggedInUser.getFirstName(),
                loggedInUser.getLastName(),
                loggedInUser.getEmail(),
                jwtToken,
                loggedInUser.getRole().getId()
        ));
    }
}
