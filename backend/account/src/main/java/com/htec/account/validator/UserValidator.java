package com.htec.account.validator;

import com.htec.account.dto.UserDTO;
import com.htec.account.exception.ExceptionSupplier;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.ObjectUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&.()–[{}]:;',?/*~$^+=<>])([^\\s]){8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public void isUserValid(UserDTO userDTO) {

        String password = userDTO.getPassword();
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw ExceptionSupplier.passwordNotValid.get();
        }

        String email = userDTO.getEmail();
        if (!EmailValidator.getInstance().isValid(email)) {
            throw ExceptionSupplier.emailNotValid.get();
        }

        String firstName = userDTO.getFirstName();

        if (ObjectUtils.isEmpty(firstName)) {
            throw ExceptionSupplier.firstNameNotValid.get();
        }

        String lastName = userDTO.getLastName();

        if (ObjectUtils.isEmpty(lastName)) {
            throw ExceptionSupplier.lastNameNotValid.get();
        }
    }

    public void isUserPasswordValid(String password) {

        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw ExceptionSupplier.passwordNotValid.get();
        }
    }
}
