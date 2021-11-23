package com.htec.shelfserver.annotation;

import com.htec.shelfserver.dto.AuthUser;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exceptionSupplier.ExceptionSupplier;
import com.htec.shelfserver.repository.UserRepository;
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
