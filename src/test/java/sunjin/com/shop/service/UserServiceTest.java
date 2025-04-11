package sunjin.com.shop.service;

import sunjin.com.shop.domain.User;
import sunjin.com.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testCreateUser() {
        User user = userService.createUser("test@test.com", "1234", "John");
        assertNotNull(user);
        assertEquals("test@test.com", user.getEmail());
        assertTrue(passwordEncoder.matches("1234", user.getPassword()));
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        userService.createUser("test@test.com", "1234", "John");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("test@test.com", "5678", "Jane");
        });
        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    }

    @Test
    void testGetUserByEmail() {
        userService.createUser("test@test.com", "1234", "John");
        User user = userService.getUserByEmail("test@test.com");
        assertNotNull(user);
        assertEquals("John", user.getName());

        User notFound = userService.getUserByEmail("notfound@test.com");
        assertNull(notFound);
    }
}