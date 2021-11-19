package com.htec.shelfserver.controller;

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
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {

        String htmlBody = tokenService.confirmToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(htmlBody);
    }

    @GetMapping(path = "/resend")
    public ResponseEntity<String> resend(@RequestParam("token") String token) {

        String htmlResponse = tokenService.createAndSendToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(htmlResponse);

    }
}
