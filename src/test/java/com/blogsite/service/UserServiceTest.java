package com.blogsite.service;

import com.blogsite.dto.UserDTO;
import com.blogsite.model.User;
import com.blogsite.repository.UserRepository;
import com.blogsite.serivce.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  // Add this line to enable Mockito
class UserServiceTest {

    @Mock
    private UserRepository userRepository;  // Mock UserRepository

    @InjectMocks
    private UserService userService;  // Inject mocked dependencies into UserService

    @BeforeEach
    void setUp() {
        // Ensure proper initialization
        assertNotNull(userRepository, "userRepository is not initialized");
        assertNotNull(userService, "userService is not initialized");
    }

    @Test
    void testRegisterUser() {
        // Arrange: create a sample UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("password123");
        userDTO.setEmail("USER@gmail.com");

        // Print to verify initialization
        System.out.println("UserDTO before passing to service: " + userDTO.getUsername());

        // Mock repository save
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(userDTO.getUsername());
        savedUser.setPassword("encodedPassword");  // Simulate encoded password
        savedUser.setEmail(userDTO.getEmail());

        // Mock the save method to return the savedUser
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act: Register the user
        UserDTO registeredUser = userService.register(userDTO);

        // Print the result
        System.out.println("Registered UserDTO: " + registeredUser.getUsername());

        // Assert: Verify the returned UserDTO matches the expected values
        assertNotNull(registeredUser);
        assertEquals("testUser", registeredUser.getUsername());
        assertEquals("USER@gmail.com", registeredUser.getEmail());
        assertEquals(1L, registeredUser.getId());
    }

//    @Test
//    void testGetUserById_NotFound() {
//        // Mocking UserRepository to return an empty Optional for a non-existing user
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Assert that the expected exception is thrown
//        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> {
//            userService.getUserById(1L);  // Attempt to get a user with ID 1, which doesn't exist
//        });
//
//        // Verify that the exception message matches
//        assertEquals("User not found with ID: 1", ex.getMessage());  // Ensure this message matches what you throw in UserService
//    }



//    @Test
//    void testGetUserById_Found() {
//        Long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//        user.setUsername("testUser");
//        user.setPassword("password123");
//        user.setRole("USER");
//
//        // Mock the findById method to return the user wrapped in Optional
//        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
//
//        // Act: Call the service method to retrieve the user
//      //  UserDTO foundUser = userService.getUserById(userId);
//
//        // Assert: Verify the returned UserDTO is correct
//        assertNotNull(foundUser);
//        assertEquals(userId, foundUser.getId());
//        assertEquals("testUser", foundUser.getUsername());
//        assertEquals("USER", foundUser.getRole());
//    }
}
