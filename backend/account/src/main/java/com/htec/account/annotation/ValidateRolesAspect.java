package com.htec.account.annotation;

import com.htec.account.dto.AuthUser;
import com.htec.account.exception.ExceptionSupplier;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ValidateRolesAspect {

    @Before("@annotation(validateRoles)")
    public void validateRoles(JoinPoint joinPoint, ValidateRoles validateRoles) throws Throwable {
        Object[] args = joinPoint.getArgs();

        AuthUser user = getAuthUser(args);

        if (user == null) {
            throw ExceptionSupplier.authenticationCredentialsNotValid.get();
        }

        if (!Arrays.asList(validateRoles.roles()).contains(Roles.fromValue(user.getRoleId()))) {
            throw ExceptionSupplier.userDoesNotHavePermission.get();
        }
    }

    private AuthUser getAuthUser(Object[] args) {
        AuthUser user = null;
        for (Object arg : args) {
            if (arg instanceof AuthUser) {
                user = (AuthUser) arg;
                break;
            }
        }
        return user;
    }
}
