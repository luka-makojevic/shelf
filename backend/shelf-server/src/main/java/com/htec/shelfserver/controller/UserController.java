package com.htec.shelfserver.controller;

import com.htec.shelfserver.annotation.AuthenticationUser;
import com.htec.shelfserver.dto.AuthUser;
import com.htec.shelfserver.model.response.TextResponseMessage;
import com.htec.shelfserver.requestModel.PasswordResetRequestModel;
import com.htec.shelfserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    final private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@AuthenticationUser AuthUser user, @PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping
    public String updateUser() {
        return "Update user is called.";
    }

    @PostMapping(value = "/password-reset-request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {

        userService.requestPasswordReset(passwordResetRequestModel.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("Email sent" , HttpStatus.OK.value()));
    }
}
