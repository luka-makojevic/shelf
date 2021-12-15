package com.htec.account.validator;

import com.htec.account.exception.ShelfException;
import com.htec.account.dto.UserDTO;
import com.htec.account.util.ErrorMessages;
import com.htec.account.validator.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
    }

    @Test
    void isUserFirstNameValid() {
        String firstName = "";
        String lastName = "Hardy";
        String email = "jhon@gmail.com";
        String password = "123.Adsf";

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        userDTO.setPassword(password);

        ShelfException shelfException = assertThrows(ShelfException.class,
                () -> userValidator.isUserValid(userDTO));

        assertEquals(ErrorMessages.FIRST_NAME_NOT_VALID.getErrorMessage(), shelfException.getMessage());
    }

    @Test
    void isUserLastNameValid() {
        String firstName = "Jhon";
        String lastName = "";
        String email = "jhon@gmail.com";
        String password = "123.Adsf";

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        userDTO.setPassword(password);

        ShelfException shelfException = assertThrows(ShelfException.class,
                () -> userValidator.isUserValid(userDTO));

        assertEquals(ErrorMessages.LAST_NAME_NOT_VALID.getErrorMessage(), shelfException.getMessage());
    }

    @Test
    void isUserEmailValid() {
        String firstName = "Jhon";
        String lastName = "Hardy";
        String email = "jhongm@com";
        String password = "123.Adsf";

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        userDTO.setPassword(password);

        ShelfException shelfException = assertThrows(ShelfException.class,
                () -> userValidator.isUserValid(userDTO));

        assertEquals(ErrorMessages.EMAIL_NOT_VALID.getErrorMessage(), shelfException.getMessage());
    }

    @Test
    void isUserPasswordValid() {
        String firstName = "Jhon";
        String lastName = "Hardy";
        String email = "jhon@gmail.com";
        String password = "12Adsf";

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        userDTO.setPassword(password);

        ShelfException shelfException = assertThrows(ShelfException.class,
                () -> userValidator.isUserValid(userDTO));

        assertEquals(ErrorMessages.PASSWORD_NOT_VALID.getErrorMessage(), shelfException.getMessage());
    }

}