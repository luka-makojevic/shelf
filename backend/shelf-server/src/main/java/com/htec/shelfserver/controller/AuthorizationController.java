package com.htec.shelfserver.controller;

import com.htec.shelfserver.annotation.AuthenticationUser;
import com.htec.shelfserver.dto.AuthUser;
import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.mapper.UserMapper;
import com.htec.shelfserver.model.request.RefreshTokenRequestModel;
import com.htec.shelfserver.model.response.RefreshTokenResponseModel;
import com.htec.shelfserver.util.TokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    private final TokenGenerator tokenGenerator;

    AuthorizationController(TokenGenerator tokenGenerator) {

        this.tokenGenerator = tokenGenerator;
    }

    @GetMapping("/refresh/token")
    public ResponseEntity getUserById(@AuthenticationUser AuthUser authUser, @RequestBody RefreshTokenRequestModel refreshTokenRequestModel) {

        UserDTO userDTO = UserMapper.INSTANCE.authUserToUserDTO(authUser);
        String jwtToken = tokenGenerator.generateJwtToken(userDTO);

        RefreshTokenResponseModel refreshTokenResponseModel = new RefreshTokenResponseModel(jwtToken);
        return ResponseEntity.status(HttpStatus.OK).body(refreshTokenResponseModel);
    }
}
