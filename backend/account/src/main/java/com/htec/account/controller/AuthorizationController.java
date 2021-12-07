package com.htec.account.controller;

import com.htec.account.annotation.AuthenticationUser;
import com.htec.account.dto.AuthUser;
import com.htec.account.dto.UserDTO;
import com.htec.account.mapper.UserMapper;
import com.htec.account.model.response.RefreshTokenResponseModel;
import com.htec.account.util.TokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    private final TokenGenerator tokenGenerator;

    public AuthorizationController(TokenGenerator tokenGenerator) {

        this.tokenGenerator = tokenGenerator;
    }

    @GetMapping("/refresh/token")
    public ResponseEntity getUserById(@AuthenticationUser AuthUser authUser) {

        UserDTO userDTO = UserMapper.INSTANCE.authUserToUserDTO(authUser);
        String jwtToken = tokenGenerator.generateJwtToken(userDTO);

        RefreshTokenResponseModel refreshTokenResponseModel = new RefreshTokenResponseModel(jwtToken);
        return ResponseEntity.status(HttpStatus.OK).body(refreshTokenResponseModel);
    }

    @GetMapping("/authenticate")
    public ResponseEntity authenticate(@AuthenticationUser AuthUser authUser) {
        return ResponseEntity.ok().build();
    }
}
