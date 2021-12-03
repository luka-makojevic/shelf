package com.htec.shelfserver.service;

import com.htec.shelfserver.dto.AuthUser;
import com.htec.shelfserver.dto.UserDTO;
import com.htec.shelfserver.entity.RoleEntity;
import com.htec.shelfserver.entity.UserEntity;
import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.exception.ShelfException;
import com.htec.shelfserver.model.response.UserPageResponseModel;
import com.htec.shelfserver.model.response.UserResponseModel;
import com.htec.shelfserver.repository.mysql.RoleRepository;
import com.htec.shelfserver.repository.mysql.UserRepository;
import com.htec.shelfserver.validator.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserValidator userValidator;

    @Mock
    EmailService emailService;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    UserService userService;

    UserEntity user;
    RoleEntity role;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        role = new RoleEntity();
    }

    @Test
    void getUserById() {

        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponseModel foundUser = userService.getUserById(1L);

        verify(userRepository).findById(anyLong());

        assertEquals(user.getId(), foundUser.getId());

    }

    @Test
    void getUserById_NotFound() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            UserResponseModel foundUser = userService.getUserById(1L);
        });

        Assertions.assertEquals("Record with provided id is not found.", exception.getMessage());

    }

    @Test
    void getUser() {

        user.setEmail("damnjan.askovic@htecgroup.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUser(user.getEmail());

        verify(userRepository).findByEmail(anyString());

        assertEquals(user.getEmail(), userDTO.getEmail());

    }

    @Test
    void getUser_NotFound() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            userService.getUser("");
        });

        Assertions.assertEquals("Record with provided email is not found.", exception.getMessage());

    }

    @Test
    void deleteUserById() {

        user.setId(1L);
        user.setRole(new RoleEntity(3L));

        when(userRepository.findById(anyLong())).thenAnswer(invocationOnMock -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(invocationOnMock.getArgument(0));
            userEntity.setRole(new RoleEntity(3L));
            return Optional.of(userEntity);
        });

        userService.deleteUserById(user.getId());

        verify(userRepository, times(1)).delete(any());

    }

    @Test
    void deleteUserById_UserDoesNotHavePermission() {

        user.setId(1L);
        user.setRole(new RoleEntity(3L));

        when(userRepository.findById(anyLong())).thenAnswer(invocationOnMock -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(invocationOnMock.getArgument(0));
            return Optional.of(userEntity);
        });

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            userService.deleteUserById(user.getId());
        });

        verify(userRepository, times(0)).delete(any());

        assertEquals("User does not have permission.", exception.getMessage());
    }


    @Test
    void getUsers() {

        user.setId(1L);
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);

        int page = 0;
        int size = 1;

        Pageable pageable = PageRequest.of(page, size);
        ArrayList<UserEntity> usersList = new ArrayList<>();
        usersList.add(user);

        when(userRepository.findAll(user.getId(), pageable)).thenReturn(new PageImpl<UserEntity>(usersList));

        UserPageResponseModel<UserResponseModel> userPageResponseModel = userService.getUsers(authUser, page + 1, size);

        verify(userRepository, times(1)).findAll(anyLong(), any(Pageable.class));
    }

    @Test
    void getUsers_WrongPage() {

        user.setId(1L);
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);

        int page = 0;
        int size = 1;

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            UserPageResponseModel<UserResponseModel> userPageResponseModel = userService.getUsers(authUser, page, size);
        });

        verify(userRepository, times(0)).findAll(anyLong(), any(Pageable.class));
        assertEquals("Page index must not be less than one.", exception.getMessage());
    }

    @Test
    void getUsers_WrongSize() {

        user.setId(1L);
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);

        int page = 1;
        int size = 0;

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            UserPageResponseModel<UserResponseModel> userPageResponseModel = userService.getUsers(authUser, page, size);
        });

        verify(userRepository, times(0)).findAll(anyLong(), any(Pageable.class));
        assertEquals("Page size must not be less than one.", exception.getMessage());
    }

    @Test
    void updateUser() {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Nikola");
        userDTO.setLastName("Nikolic");
        userDTO.setPassword("Asdfg.123");

        user.setSalt(String.valueOf(new Random().nextInt()));

        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(user));

        UserResponseModel userResponseModel = userService.updateUser(userDTO);

        verify(userRepository, times(1)).save(user);
        verify(userValidator, times(1)).isUserPasswordValid(userDTO.getPassword());
        verify(bCryptPasswordEncoder, times(1)).encode(userDTO.getPassword() + user.getSalt());

        assertEquals(userResponseModel.getFirstName(), userDTO.getFirstName());
    }

    @Test
    void updateUser_UserNotFound() {

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Nikola");
        userDTO.setLastName("Nikolic");
        userDTO.setPassword("Asdfg.123");


        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            UserResponseModel userResponseModel = userService.updateUser(userDTO);
        });

        verify(userRepository, times(0)).save(any());
        verify(userValidator, times(0)).isUserPasswordValid(userDTO.getPassword());
        verify(bCryptPasswordEncoder, times(0)).encode(userDTO.getPassword() + "Adasdasdas");

        assertEquals("Record with provided id is not found.", exception.getMessage());
    }

    @Test
    void updateUser_PasswordNotValid() {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Nikola");
        userDTO.setLastName("Nikolic");
        userDTO.setPassword("s.123");

        user.setSalt(String.valueOf(new Random().nextInt()));

        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(user));
        doThrow(ExceptionSupplier.passwordNotValid.get()).when(userValidator).isUserPasswordValid(userDTO.getPassword());
        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            UserResponseModel userResponseModel = userService.updateUser(userDTO);
        });

        verify(userValidator, times(1)).isUserPasswordValid(userDTO.getPassword());
        verify(userRepository, times(0)).save(any());
        verify(bCryptPasswordEncoder, times(0)).encode(userDTO.getPassword() + user.getSalt());

        assertEquals("Password is not valid.", exception.getMessage());
    }

    @Test
    void updateUserRole() {

        user.setId(1L);
        user.setEmail("nikola@gmail.com");
        user.setFirstName("Nikola");
        user.setRole(new RoleEntity(3L));
        role.setId(1L);
        role.setName("name");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));

        UserResponseModel userResponseModel = userService.updateUserRole(user.getId(), role.getId());

        verify(userRepository, times(1)).save(user);
        verify(roleRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(emailService, times(1)).sendEmail(eq(user.getEmail()), anyMap(), anyString(), anyString());

        assertEquals(userResponseModel.getRole().getId(), role.getId());

    }

    @Test
    void updateUserRole_UserAlreadyHasThatRole() {

        user.setId(1L);
        user.setRole(new RoleEntity(1L));
        role.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            UserResponseModel userResponseModel = userService.updateUserRole(user.getId(), role.getId());
        });


        verify(userRepository, times(0)).save(user);
        verify(roleRepository, times(0)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(emailService, times(0)).sendEmail(anyString(), anyMap(), anyString(), anyString());

        assertEquals("User already has that role.", exception.getMessage());
    }

    @Test
    void updateUserRole_UserNotFound() {

        user.setId(1L);
        user.setRole(new RoleEntity(1L));
        role.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            UserResponseModel userResponseModel = userService.updateUserRole(user.getId(), role.getId());
        });


        verify(userRepository, times(0)).save(user);
        verify(roleRepository, times(0)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(emailService, times(0)).sendEmail(anyString(), anyMap(), anyString(), anyString());

        assertEquals("Record with provided id is not found.", exception.getMessage());
    }

    @Test
    void updateUserRole_RoleNotFound() {

        user.setId(1L);
        user.setRole(new RoleEntity(1L));
        role.setId(2L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(roleRepository.findById(role.getId())).thenReturn(Optional.empty());

        ShelfException exception = Assertions.assertThrows(ShelfException.class, () -> {
            userService.updateUserRole(user.getId(), role.getId());
        });


        verify(userRepository, times(0)).save(user);
        verify(roleRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(emailService, times(0)).sendEmail(anyString(), anyMap(), anyString(), anyString());

        assertEquals("Record with provided id is not found.", exception.getMessage());
    }
}