package com.htec.account.annotation;

import com.htec.account.dto.AuthUser;
import com.htec.account.entity.UserEntity;
import com.htec.account.exception.ExceptionSupplier;
import com.htec.account.repository.mysql.UserRepository;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    public AuthUserArgumentResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthenticationUser.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Principal userPrincipal = webRequest.getUserPrincipal();

        if (userPrincipal == null) {
            throw ExceptionSupplier.authenticationCredentialsNotValid.get();
        }

        UserEntity userEntity = userRepository.findByEmail(userPrincipal.getName())
                .orElseThrow(ExceptionSupplier.authenticationCredentialsNotValid);

        return new AuthUser(userEntity.getId(), userEntity.getEmail(), userEntity.getRole().getId());
    }
}
