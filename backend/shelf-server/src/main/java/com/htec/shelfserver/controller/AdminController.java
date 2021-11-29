package com.htec.shelfserver.controller;

import com.htec.shelfserver.annotation.AuthenticationUser;
import com.htec.shelfserver.annotation.ValidateRoles;
import com.htec.shelfserver.dto.AuthUser;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.model.response.TextResponseMessage;
import com.htec.shelfserver.service.UserService;
import com.htec.shelfserver.util.Roles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ValidateRoles(roles = {Roles.USER})
    public ResponseEntity getUsers(@AuthenticationUser AuthUser user,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "5") Integer size) {
        return ResponseEntity.ok(userService.getUsers(page, size));
    }

    @GetMapping("/{id}")
    @ValidateRoles(roles = {Roles.SUPER_ADMIN})
    public ResponseEntity getUserById(@AuthenticationUser AuthUser user, @PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    @ValidateRoles(roles = {Roles.USER})
    public ResponseEntity<TextResponseMessage> deleteUserById(@AuthenticationUser AuthUser user, @PathVariable Long id) {

        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("User deleted", HttpStatus.OK.value()));
    }
}
