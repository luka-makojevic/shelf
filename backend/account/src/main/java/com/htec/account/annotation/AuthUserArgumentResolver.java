package com.htec.account.annotation;

import com.htec.account.dto.AuthUser;
import com.htec.account.exception.ExceptionSupplier;
import com.htec.account.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthenticationUser.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String jwtToken = webRequest.getHeader(SecurityConstants.AUTHORIZATION_HEADER_STRING);

        if (jwtToken == null)
            throw ExceptionSupplier.tokenNotFound.get();

        jwtToken = jwtToken.replace(SecurityConstants.BEARER_TOKEN_PREFIX, "");

        String email;

        Claims body = Jwts.parser()
                .setSigningKey(SecurityConstants.TOKEN_SECRET)
                .parseClaimsJws(jwtToken)
                .getBody();

        email = body
                .getSubject();

        if (email.isEmpty()) {
            throw ExceptionSupplier.authenticationCredentialsNotValid.get();
        }

        Long userId = Long.valueOf(body.getId());
        Long roleId = body.get("role_id", Long.class);

        return new AuthUser(userId, email, roleId);
    }
}
