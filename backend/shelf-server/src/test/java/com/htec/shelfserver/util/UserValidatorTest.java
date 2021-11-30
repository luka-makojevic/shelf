package com.htec.shelfserver.util;

import com.htec.shelfserver.exception.ShelfException;
import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.validator.UserValidator;
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

        assertEquals("First name is not valid."  , shelfException.getMessage());
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

        assertEquals("Last name is not valid."  , shelfException.getMessage());
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

        assertEquals("Email is not valid."  , shelfException.getMessage());
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

        assertEquals("Password is not valid."  , shelfException.getMessage());
    }

}