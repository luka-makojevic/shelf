package com.htec.account.controller;

import com.htec.account.model.response.RefreshTokenResponseModel;
import com.htec.account.service.UserService;
import com.htec.account.util.TokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    private final TokenGenerator tokenGenerator;
    private final UserService userService;

    public AuthorizationController(TokenGenerator tokenGenerator,
                                   UserService userService) {

        this.tokenGenerator = tokenGenerator;
        this.userService = userService;
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<RefreshTokenResponseModel> getNewAccessToken(@RequestBody String refreshToken) {

        RefreshTokenResponseModel refreshTokenResponseModel = userService.sendNewAccessToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(refreshTokenResponseModel);
    }

    @GetMapping("/authenticate")
    public ResponseEntity authenticate() {
        return ResponseEntity.ok().build();
    }
}
