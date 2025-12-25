package com.blogsite.repository;

import com.blogsite.model.Blog;
import com.blogsite.model.User;
import com.blogsite.serivce.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class BlogRepositoryTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BlogService blogService;  // Inject mocks into service layer

    private Blog blog;
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks

        // Create and save a User
        user = new User();
        user.setUsername("author1");
        user.setPassword("author123");
        user.setRole("AUTHOR");

        // Create a Blog object and associate it with the user
        blog = new Blog();
        blog.setTitle("Spring Boot Basics " + System.currentTimeMillis());  // Ensure unique title
        blog.setCategory("Technology");
        blog.setContent("Content about Spring Boot.");
        blog.setUser(user);
    }

    @Test
    public void testSaveBlog() {
        // Mock the save method to return a blog with a non-null ID
        Blog savedBlog = new Blog();
        savedBlog.setId(1L);  // Simulate that the blog ID is generated
        when(blogRepository.save(any(Blog.class))).thenReturn(savedBlog);

        // Save the blog using the repository
        Blog result = blogRepository.save(blog);

        // Assert that the ID is not null
        assertNotNull(result.getId(), "Blog ID should not be null");

        // Verify the save method was called exactly once
        verify(blogRepository, times(1)).save(any(Blog.class));
    }

    @Test
    public void testFindByCategory() {
        // Mock the repository method to return a list of blogs by category
        when(blogRepository.findByCategory("Technology")).thenReturn(Arrays.asList(blog));

        // Call the method to retrieve blogs by category
        List<Blog> blogs = blogRepository.findByCategory("Technology");

        // Assert the list is not null and contains at least one blog
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0, "Blog list should contain saved blogs.");

        // Verify that the findByCategory method was called exactly once
        verify(blogRepository, times(1)).findByCategory("Technology");
    }
}
