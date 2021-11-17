package com.htec.shelfserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htec.shelfserver.config.SpringApplicationContext;
import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ShelfException;
import com.htec.shelfserver.exceptionSupplier.ExceptionSupplier;
import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.requestModel.UserLoginRequestModel;
import com.htec.shelfserver.responseModel.UserLoginResponseModel;
import com.htec.shelfserver.service.UserService;
import com.htec.shelfserver.util.ErrorMessages;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private String contentType;

    @Autowired
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {

        try {

            contentType = req.getHeader("Accept");

            UserLoginRequestModel creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UserLoginRequestModel.class);


            UserService userService = (UserService) SpringApplicationContext.getBean("userService");
            UserRepository userRepository = (UserRepository) SpringApplicationContext.getBean("userRepository");


            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword() + userService.getUser(creds.getEmail()).getSalt(),
                            new ArrayList<>())
            );
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String userName = ((User) auth.getPrincipal()).getUsername();

        UserService userService = (UserService) SpringApplicationContext.getBean("userService");
        UserDTO loginUser = userService.getUser(userName);

        if (loginUser.getEmailVerified()) {

            String token = Jwts.builder()
                    .setSubject(userName)
                    .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                    .compact();

            res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);

            UserLoginResponseModel userResponse = new UserLoginResponseModel(loginUser.getId(),
                    loginUser.getFirstName(), loginUser.getLastName(), loginUser.getEmail(), loginUser.getRole().getId());

            String userResponseJson = new ObjectMapper().writeValueAsString(userResponse);
            res.setContentType("application/json");
            res.getWriter().write(userResponseJson);
        }
        else {
            ShelfException ex = ExceptionSupplier.emailNotVerified.get();
            String userResponseJson = new ObjectMapper().writeValueAsString(ex);
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.setContentType("application/json");
            res.getWriter().write(userResponseJson);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req,
                                              HttpServletResponse res,
                                              AuthenticationException failed) throws IOException, ServletException {

        ShelfException ex = ExceptionSupplier.passwordNotValid.get();
        String userResponseJson = new ObjectMapper().writeValueAsString(ex);
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json");
        res.getWriter().write(userResponseJson);
    }
}
