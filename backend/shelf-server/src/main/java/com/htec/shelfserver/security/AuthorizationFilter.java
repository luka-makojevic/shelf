package com.htec.shelfserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.exception.ShelfException;
import com.htec.shelfserver.model.response.ErrorMessage;
import com.htec.shelfserver.repository.cassandra.InvalidJwtTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final InvalidJwtTokenRepository invalidJwtTokenRepository;

    public AuthorizationFilter(AuthenticationManager authenticationManager,
                               InvalidJwtTokenRepository invalidJwtTokenRepository) {

        super(authenticationManager);
        this.invalidJwtTokenRepository = invalidJwtTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(SecurityConstants.AUTHORIZATION_HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = null;
        try {

            authentication = getAuthentication(req);
        }catch (ShelfException e){
            ShelfException ex = ExceptionSupplier.userIsNotLoggedIn.get();
            ErrorMessage jsonResponse = new ErrorMessage(ex.getMessage(), ex.getStatus(), ex.getTimestamp(), ex.getErrorMessage());
            String userResponseJson = new ObjectMapper().writeValueAsString(jsonResponse);
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setContentType("application/json");
            res.getWriter().write(userResponseJson);
            return;
        }
        catch (ExpiredJwtException e) {

            ShelfException ex = ExceptionSupplier.tokenExpired.get();
            ErrorMessage jsonResponse = new ErrorMessage(ex.getMessage(), ex.getStatus(), ex.getTimestamp(), ex.getErrorMessage());
            String userResponseJson = new ObjectMapper().writeValueAsString(jsonResponse);
            res.setStatus(HttpStatus.FORBIDDEN.value());
            res.setContentType("application/json");
            res.getWriter().write(userResponseJson);
            return;
        } catch (Exception e) {

            ShelfException ex = ExceptionSupplier.tokenNotValid.get();
            ErrorMessage jsonResponse = new ErrorMessage(ex.getMessage(), ex.getStatus(), ex.getTimestamp(), ex.getErrorMessage());
            String userResponseJson = new ObjectMapper().writeValueAsString(jsonResponse);
            res.setStatus(HttpStatus.FORBIDDEN.value());
            res.setContentType("application/json");
            res.getWriter().write(userResponseJson);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER_STRING);

        if (token != null) {

            token = token.replace(SecurityConstants.BEARER_TOKEN_PREFIX, "");

            if (invalidJwtTokenRepository.findByJwt(token).isPresent()) {
                throw new ShelfException();
            }

            String user;

            user = Jwts.parser()
                    .setSigningKey(SecurityConstants.TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();


            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }

            return null;
        }

        return null;
    }
}
