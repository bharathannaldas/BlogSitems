package com.blogsite.service;

import com.blogsite.dto.BlogDTO;
import com.blogsite.dto.BlogResponse;
import com.blogsite.exception.BlogNotFoundException;
import com.blogsite.model.Blog;
import com.blogsite.model.User;
import com.blogsite.repository.BlogRepository;
import com.blogsite.serivce.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogService blogService;

    private Blog blog;
    private User user;

    @BeforeEach
    void setUp() {
        // Initialize User and Blog objects for the test
        user = new User();
        user.setId(1L);
        user.setUsername("bharath");

        blog = new Blog();
        blog.setId(1L);
        blog.setTitle("Test Blog");
        blog.setCategory("Tech");
        blog.setContent("Content");
        blog.setUser(user);
        blog.setCreatedDate(LocalDate.now());
    }

    // ---------- addBlog ----------

    @Test
    void addBlog_success() {
        // Arrange: Prepare a valid BlogDTO
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setTitle("Test Blog");
        blogDTO.setCategory("Technology");
        blogDTO.setContent("This is a test blog content");

        // Prepare the expected result as a Blog entity
        Blog expectedBlog = new Blog();
        expectedBlog.setTitle(blogDTO.getTitle());
        expectedBlog.setCategory(blogDTO.getCategory());
        expectedBlog.setContent(blogDTO.getContent());
        expectedBlog.setStatus("PENDING");
        expectedBlog.setCreatedDate(LocalDate.now());

        // Mock the repository to return the expected Blog
        when(blogRepository.save(any(Blog.class))).thenReturn(expectedBlog);

        // Act: Call the service method
        Blog result = blogService.addBlog(blogDTO);

        // Assert: Validate the result
        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(blogDTO.getTitle(), result.getTitle());
        assertEquals(blogDTO.getCategory(), result.getCategory());

        // Verify that the repository save method was called once
        verify(blogRepository, times(1)).save(any(Blog.class));
    }

    @Test
    void addBlog_exception() {
        // Arrange: Prepare a valid BlogDTO
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setTitle("Test Blog");
        blogDTO.setCategory("Technology");
        blogDTO.setContent("This is a test blog content");

        // Mock the repository to throw an exception when saving
        when(blogRepository.save(any(Blog.class))).thenThrow(new RuntimeException("DB error"));

        // Act & Assert: Ensure the service method throws the expected exception
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            blogService.addBlog(blogDTO);
        });

        // Assert: Validate the exception message
        assertEquals("Error adding the blog.", ex.getMessage());
    }

    // ---------- getBlogsByCategory ----------

    @Test
    void getBlogsByCategory_success() {
        when(blogRepository.findByCategory("Tech"))
                .thenReturn(Arrays.asList(blog));

        List<Blog> blogs = blogService.getBlogsByCategory("Tech");

        assertEquals(1, blogs.size());
        verify(blogRepository).findByCategory("Tech");
    }

    @Test
    void getBlogsByCategory_empty() {
        when(blogRepository.findByCategory("NonExistent"))
                .thenReturn(Arrays.asList());

        List<Blog> blogs = blogService.getBlogsByCategory("NonExistent");

        assertTrue(blogs.isEmpty());
        verify(blogRepository).findByCategory("NonExistent");
    }

    // ---------- getBlogsByUser ----------

    @Test
    void getBlogsByUser_success() {
        when(blogRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(blog));

        List<Blog> blogs = blogService.getBlogsByUser(1L);

        assertEquals(1, blogs.size());
        verify(blogRepository).findByUserId(1L);
    }

    // ---------- getAllBlogs ----------

    @Test
    void getAllBlogs_success() {
        when(blogRepository.findAll()).thenReturn(Arrays.asList(blog));

        List<Blog> blogs = blogService.getAllBlogs();

        assertEquals(1, blogs.size());
        verify(blogRepository).findAll();
    }

    // ---------- deleteByName ----------

    @Test
    void deleteByName_success() {
        doNothing().when(blogRepository).deleteByTitle("Test Blog");

        blogService.deleteByName("Test Blog");

        verify(blogRepository).deleteByTitle("Test Blog");
    }

    @Test
    void deleteByName_invalidTitle() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            blogService.deleteByName(null); // Simulating the scenario where title is null
        });

        assertEquals("Blog title cannot be null", ex.getMessage());
    }


    @Test
    void deleteByName_blogNotFound() {
        String blogName = "NonExistent Blog";

        // Mock the repository to throw BlogNotFoundException when a non-existent blog is being deleted
        doThrow(new BlogNotFoundException("Blog not found with title: " + blogName))
                .when(blogRepository).deleteByTitle(blogName);
        BlogNotFoundException ex = assertThrows(BlogNotFoundException.class, () -> {
            blogService.deleteByName(blogName); // Simulating deletion of a non-existent blog
        });

        assertEquals("Blog not found with title: NonExistent Blog", ex.getMessage());
    }


    // ---------- updateBlog ----------

    @Test
    void updateBlog_success() {
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));
        when(blogRepository.save(any(Blog.class))).thenReturn(blog);

        Blog updated = blogService.updateBlog(1L, "Updated Content", "Java");

        assertEquals("Updated Content", updated.getContent());
        assertEquals("Java", updated.getCategory());
    }

    @Test
    void updateBlog_notFound() {
        // Mock the repository to return Optional.empty() for the non-existent blog ID
        when(blogRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Ensure that the BlogNotFoundException is thrown
        assertThrows(BlogNotFoundException.class, () -> {
            blogService.updateBlog(1L, "Updated Content", "Java");
        });
    }

    // ---------- getBlogsByCategoryAndDuration ----------

    @Test
    void getBlogsByCategoryAndDuration_success() {
        when(blogRepository.findByCategoryAndCreatedDateBetween(
                anyString(), any(), any()))
                .thenReturn(Arrays.asList(blog));

        List<BlogResponse> responses =
                blogService.getBlogsByCategoryAndDuration(
                        "Tech", "2024-01-01", "2025-01-01");

        assertEquals(1, responses.size());
        assertEquals("Test Blog", responses.get(0).getTitle());
    }

    @Test
    void getBlogsByCategoryAndDuration_emptyCategory() {
        when(blogRepository.findByCategoryAndCreatedDateBetween(
                "", LocalDate.parse("2024-01-01"), LocalDate.parse("2025-01-01"))
        ).thenReturn(Arrays.asList()); // Mock the empty result for empty category

        List<BlogResponse> responses =
                blogService.getBlogsByCategoryAndDuration(
                        "", "2024-01-01", "2025-01-01");

        assertTrue(responses.isEmpty());
    }


    @Test
    void getBlogsByCategoryAndDuration_invalidDateRange() {
        when(blogRepository.findByCategoryAndCreatedDateBetween(
                "Tech", LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1)))
                .thenReturn(Arrays.asList(blog));

        List<BlogResponse> responses =
                blogService.getBlogsByCategoryAndDuration(
                        "Tech", "2024-01-01", "2025-01-01");

        assertEquals(1, responses.size());
        assertEquals("Test Blog", responses.get(0).getTitle());
    }
}
