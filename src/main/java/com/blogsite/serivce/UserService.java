package com.blogsite.serivce;

import com.blogsite.dto.UserDTO;
import com.blogsite.model.User;
import com.blogsite.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register user and handle exception if any
    public UserDTO register(UserDTO request) {
        try {
            logger.info("Registering a new user: {}", request.getUsername());

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());   // 👈 hashed password stored
            user.setRole(request.getRole());
            User savedUser = userRepository.save(user);
            return new UserDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getPassword(),savedUser.getRole());
        } catch (Exception ex) {
            //logger.error("Error occurred while registering user: {}", user.getUsername(), ex);
            throw new RuntimeException("Error registering the user");
        }
    }

    // Get a user by ID and handle exception if user is not found
//    public UserDTO getUserById(Long userId) {
//        try {
//            logger.info("Fetching user with ID: {}", userId);
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
//            return new UserDTO(user.getId(), user.getUsername(),user.getRole());
//        } catch (UserNotFoundException ex) {
//            logger.error("User not found: {}", userId, ex);
//            throw ex;  // Rethrow the exception to be handled globally
//        } catch (Exception ex) {
//            logger.error("Error fetching user by ID: {}", userId, ex);
//            throw new RuntimeException("Error fetching user");
//        }
//    }
}
