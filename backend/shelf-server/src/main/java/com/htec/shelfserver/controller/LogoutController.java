package com.htec.shelfserver.controller;

import com.htec.shelfserver.model.request.UserLogoutRequestModel;
import com.htec.shelfserver.model.response.TextResponseMessage;
import com.htec.shelfserver.service.LogoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class LogoutController {

    private final LogoutService logoutService;

    public LogoutController(LogoutService logoutService) {

        this.logoutService = logoutService;
    }

    @PostMapping("/logout")
    public ResponseEntity logoutUser(@RequestBody UserLogoutRequestModel userLoginRequestModel,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {

        String jwtToken = userLoginRequestModel.getJwtToken();
        String jwtRefreshToken = userLoginRequestModel.getJwtRefreshToken();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            logoutService.invalidateJwtTokens(jwtToken, jwtRefreshToken);
        }
        SecurityContextHolder.getContext().setAuthentication(null);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("Logged out successfully", HttpStatus.OK.value()));
    }
}
