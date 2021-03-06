package com.htec.account.controller;

import com.htec.account.model.request.ConfirmTokenRequestModel;
import com.htec.account.model.response.TextResponseMessage;
import com.htec.account.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tokens")
@AllArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping(path = "/confirmation")
    public ResponseEntity<TextResponseMessage> confirm(@RequestBody ConfirmTokenRequestModel tokenJSON) {

        String response = tokenService.confirmToken(tokenJSON.getToken());

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(response, HttpStatus.OK.value()));
    }

    @PostMapping(path = "/resend")
    public ResponseEntity<TextResponseMessage> resend(@RequestBody ConfirmTokenRequestModel tokenJSON) {

        String response = tokenService.resendToken(tokenJSON.getToken());

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(response, HttpStatus.OK.value()));
    }
}
