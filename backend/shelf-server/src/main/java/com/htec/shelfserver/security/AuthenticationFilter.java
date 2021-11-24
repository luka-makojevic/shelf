package com.htec.shelfserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htec.shelfserver.config.SpringApplicationContext;
import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ShelfException;
import com.htec.shelfserver.exceptionSupplier.ExceptionSupplier;
import com.htec.shelfserver.repository.UserRepository;
import com.htec.shelfserver.requestModel.UserLoginRequestModel;
import com.htec.shelfserver.responseModel.ErrorMessage;
import com.htec.shelfserver.responseModel.UserLoginResponseModel;
import com.htec.shelfserver.service.UserService;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private String contentType;
    private String token;

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
            Optional<UserEntity> userEntity = userRepository.findByEmail(creds.getEmail());
            String salt = userEntity.isPresent() ? userEntity.get().getSalt() : "";


            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword() + salt,
                            new ArrayList<>())
            );
        } catch (IOException e) {
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

            token = Jwts.builder()
                    .setId(loginUser.getId().toString())
                    .setSubject(userName)
                    .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                    .claim("role_id", loginUser.getRole().getId())
                    .compact();


            res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);

            UserLoginResponseModel userResponse = new UserLoginResponseModel(loginUser.getId(),
                    loginUser.getFirstName(), loginUser.getLastName(), loginUser.getEmail(), token, loginUser.getRole().getId());

            String userResponseJson = new ObjectMapper().writeValueAsString(userResponse);
            res.setContentType("application/json");
            res.getWriter().write(userResponseJson);
        } else {
            ShelfException ex = ExceptionSupplier.emailNotVerified.get();
            ErrorMessage jsonResponse = new ErrorMessage(ex.getMessage(), ex.getStatus(), ex.getTimestamp(), ex.getErrorMessage());
            String userResponseJson = new ObjectMapper().writeValueAsString(jsonResponse);
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.setContentType("application/json");
            res.getWriter().write(userResponseJson);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req,
                                              HttpServletResponse res,
                                              AuthenticationException failed) throws IOException, ServletException {

        ShelfException ex = ExceptionSupplier.authenticationCredentialsNotValid.get();
        ErrorMessage jsonResponse = new ErrorMessage(ex.getMessage(), ex.getStatus(), ex.getTimestamp(), ex.getErrorMessage());
        String userResponseJson = new ObjectMapper().writeValueAsString(jsonResponse);
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json;");
        res.getWriter().write(userResponseJson);
    }

    public void decodeTokenParts(String token) {
        String[] parts = token.split("\\.", 0);

        for (String part : parts) {
            byte[] bytes = Base64.getUrlDecoder().decode(part);
            String decodedString = new String(bytes, StandardCharsets.UTF_8);
        }
    }
}
