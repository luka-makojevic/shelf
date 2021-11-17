package com.htec.shelfserver.util;

import com.htec.shelfserver.exception.BadRequestException;
import com.htec.shelfserver.dto.UserDTO;
import org.springframework.util.ObjectUtils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&.()–[{}]:;',?/*~$^+=<>])([^\\s]){8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public void isUserValid(UserDTO userDTO) throws BadRequestException {

        String password = userDTO.getPassword();
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new BadRequestException(ErrorMessages.PASSWORD_NOT_VALID.getErrorMessage());
        }

        String email = userDTO.getEmail();
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        if (!result) {
            throw new BadRequestException(ErrorMessages.EMAIL_NOT_VALID.getErrorMessage());
        }

        String firstName = userDTO.getFirstName();

        if (ObjectUtils.isEmpty(firstName)) {
            throw new BadRequestException(ErrorMessages.FIRST_NAME_NOT_VALID.getErrorMessage());
        }

        String lastName = userDTO.getLastName();

        if (ObjectUtils.isEmpty(lastName)) {
            throw new BadRequestException(ErrorMessages.LAST_NAME_NOT_VALID.getErrorMessage());
        }
    }
}
