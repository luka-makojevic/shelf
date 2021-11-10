package com.htec.shelfserver.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    @GetMapping
    public String getUsers() {
        return "Get users is called.";
    }

    @PostMapping
    public String createUser() {
        return "Create users is called.";
    }

    @PutMapping
    public String updateUser() {
        return "Update user is called.";
    }

    @DeleteMapping
    public String deleteUser() {
        return "Delete user is called.";
    }
}
