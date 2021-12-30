package com.htec.account.controller;

import com.htec.account.annotation.AuthenticationUser;
import com.htec.account.dto.AuthUser;
import com.htec.account.dto.UserDTO;
import com.htec.account.mapper.UserMapper;
import com.htec.account.model.request.PasswordResetModel;
import com.htec.account.model.request.PasswordResetRequestModel;
import com.htec.account.model.request.UpdateUserPhotoByIdRequestModel;
import com.htec.account.model.request.UserUpdateRequestModel;
import com.htec.account.model.response.TextResponseMessage;
import com.htec.account.service.UserService;
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

    @GetMapping("/me")
    public ResponseEntity getMe(@AuthenticationUser AuthUser user) {

        return ResponseEntity.ok(userService.getMe(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@AuthenticationUser AuthUser user, @PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable Long id,
                                     @RequestBody UserUpdateRequestModel userUpdateRequestModel) {

        UserDTO userDTO = UserMapper.INSTANCE.userUpdateRequestModelToUserDto(userUpdateRequestModel);
        userDTO.setId(id);

        userService.updateUser(userDTO);

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping(value = "/password-reset-request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity requestResetPassword(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {

        userService.requestPasswordReset(passwordResetRequestModel.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("Email sent", HttpStatus.OK.value()));
    }

    @PostMapping(value = "/password-reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity resetPassword(@RequestBody PasswordResetModel passwordResetModel) {

        userService.resetPassword(passwordResetModel);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("User's password changed successfully", HttpStatus.OK.value()));
    }

    @PutMapping("/update/photo")
    public ResponseEntity updateUserPhotoById(@RequestBody UpdateUserPhotoByIdRequestModel updateUserPhotoByIdRequestModel) {

        userService.updateUserProfilePicture(updateUserPhotoByIdRequestModel);

        return ResponseEntity.ok(userService.getUserById(updateUserPhotoByIdRequestModel.getId()));
    }

    @GetMapping("/picture-name/{userId}")
    public String pictureNamePath(@PathVariable Long userId) {

        return userService.getPicturePathByUserId(userId);
    }
}
