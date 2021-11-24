package com.htec.shelfserver.controller;

import com.htec.shelfserver.annotation.AuthenticationUser;
import com.htec.shelfserver.annotation.ValidateRoles;
import com.htec.shelfserver.dto.AuthUser;
import com.htec.shelfserver.enumes.Roles;
import com.htec.shelfserver.service.UserService;
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
    @ValidateRoles(roles = {Roles.SUPER_ADMIN})
    public ResponseEntity getUsers(@AuthenticationUser AuthUser user) {

        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    @ValidateRoles(roles = {Roles.SUPER_ADMIN})
    public ResponseEntity getUserById(@AuthenticationUser AuthUser user, @PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    @ValidateRoles(roles = {Roles.USER})
    public ResponseEntity deleteUserById(@AuthenticationUser AuthUser user, @PathVariable Long id) {

        userService.deleteUserById(id);
        return ResponseEntity.ok().body("User deleted");
    }
}
