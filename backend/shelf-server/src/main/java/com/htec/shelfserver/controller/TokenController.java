package com.htec.shelfserver.controller;

import com.htec.shelfserver.model.responseModel.TextResponseMessage;
import com.htec.shelfserver.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/register")
@AllArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping(path = "/confirmation")
    public ResponseEntity<TextResponseMessage> confirm(@RequestParam("token") String token) {

        String response = tokenService.confirmToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(response , HttpStatus.OK.value()));
    }

    @GetMapping(path = "/resend")
    public ResponseEntity<TextResponseMessage> resend(@RequestParam("token") String token) {

        String response = tokenService.createAndSendToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(response , HttpStatus.OK.value()));

    }
}
