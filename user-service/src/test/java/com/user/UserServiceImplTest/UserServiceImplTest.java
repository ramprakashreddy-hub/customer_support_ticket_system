package com.user.UserServiceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.*;

import com.user.config.JWTUtils;
import com.user.dto.User;
import com.user.dto.UserMapper;
import com.user.entity.UserEntity;
import com.user.exception.EmailAlreadyExistException;
import com.user.exception.EmailNotFoundException;
import com.user.exception.UserNotFoundException;
import com.user.repository.UserRepository;
import com.user.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {

    @Mock private UserRepository userRepo;
    @Mock private PasswordEncoder encoder;
    @Mock private JWTUtils jwtUtils;
    @Mock private UserMapper mapper;

    @InjectMocks private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        String name = "test";
        String email = "test@mail.com";
        String password = "password";
        String role = "CUSTOMER";

        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encoded_password");

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setName(name);
        userEntity.setEmail(email);
        userEntity.setPassword("encoded_password");
        userEntity.setRole("CUSTOMER");

        when(userRepo.save(any(UserEntity.class))).thenReturn(userEntity);

        User expectedUser = new User();
        when(mapper.toDomain(userEntity)).thenReturn(expectedUser);

        User result = userService.register(name, email, password, role);
        assertNotNull(result);
        verify(userRepo).save(any(UserEntity.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        String email = "test@mail.com";
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(new UserEntity()));
        assertThrows(EmailAlreadyExistException.class, () ->
                userService.register("test", email, "pass", "CUSTOMER"));
    }

    @Test
    void testLogin_Success() {
        String email = "test@mail.com";
        String password = "password";
        String encodedPassword = "encoded_password";

        UserEntity user = new UserEntity();
        user.setUserId("uid123");
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(encoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtUtils.generateToken(user)).thenReturn("jwt-token");

        String token = userService.login(email, password);
        assertEquals("jwt-token", token);
    }

    @Test
    void testLogin_EmailNotFound() {
        when(userRepo.findByEmail("wrong@mail.com")).thenReturn(Optional.empty());
        assertThrows(EmailNotFoundException.class, () -> userService.login("wrong@mail.com", "pass"));
    }

    @Test
    void testLogin_WrongPassword() {
        UserEntity user = new UserEntity();
        user.setPassword("encoded");

        when(userRepo.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.login("test@mail.com", "wrong"));
    }

    @Test
    void testGetProfile_Success() {
        String userId = "123";
        UserEntity entity = new UserEntity();
        when(userRepo.findById(userId)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(new User());

        User result = userService.getProfile(userId);
        assertNotNull(result);
    }

    @Test
    void testGetProfile_NotFound() {
        when(userRepo.findById("notfound")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getProfile("notfound"));
    }

    @Test
    void testGetAllUsers() {
        List<UserEntity> entities = List.of(new UserEntity(), new UserEntity());
        when(userRepo.findAll()).thenReturn(entities);
        when(mapper.toDomainList(entities)).thenReturn(List.of(new User(), new User()));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testUpdateUserRole() {
        UserEntity user = new UserEntity();
        when(userRepo.findById("id1")).thenReturn(Optional.of(user));

        userService.updateUserRole("id1", "ADMIN");

        assertEquals("ADMIN", user.getRole());
        verify(userRepo).save(user);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser("id1");
        verify(userRepo).deleteById("id1");
    }

    @Test
    void testGetAvailableRole_Invalid() {
        assertThrows(IllegalArgumentException.class, () -> userService.getAvailableRole("INVALID_ROLE"));
    }

    @Test
    void testGetAvailableRole_Valid() {
        String result = userService.getAvailableRole("admin");
        assertEquals("ADMIN", result);
    }
}
