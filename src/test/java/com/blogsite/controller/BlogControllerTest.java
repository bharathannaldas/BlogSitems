package com.blogsite.controller;

import com.blogsite.dto.BlogDTO;
import com.blogsite.dto.BlogResponse;
import com.blogsite.model.Blog;
import com.blogsite.serivce.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogController.class)
@AutoConfigureMockMvc(addFilters = false)
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addBlog_success() throws Exception {
        // Prepare test blog object
        Blog blog = new Blog();
        blog.setTitle("test");
        blog.setCategory("tech");

        // Mock the service method to return the test blog
        when(blogService.addBlog(any(BlogDTO.class))).thenReturn(blog);

        // Perform the request and validate the response
        mockMvc.perform(post("/api/v1.0/blogsite/user/blogs/add/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blog))) // Convert blog to JSON
                .andExpect(status().isOk()) // Expect OK status
                .andExpect(jsonPath("$.title").value("test")); // Expect title field in the response
    }

    @Test
    void getAllBlogs_success() throws Exception {
        // Mock the service method to return a list of blogs
        when(blogService.getAllBlogs()).thenReturn(List.of(new Blog()));

        // Perform GET request and validate the response
        mockMvc.perform(get("/api/v1.0/blogsite/user/getall"))
                .andExpect(status().isOk()); // Expect OK status
    }

    @Test
    void getBlogsByCategory_success() throws Exception {
        // Mock the service method to return a list of blogs for category "tech"
        when(blogService.getBlogsByCategory("tech"))
                .thenReturn(List.of(new Blog()));

        // Perform GET request for category "tech" and validate the response
        mockMvc.perform(get("/api/v1.0/blogsite/blogs/info/tech"))
                .andExpect(status().isOk()); // Expect OK status
    }

    @Test
    void deleteBlog_success() throws Exception {
        // Perform DELETE request and validate the response
        mockMvc.perform(delete("/api/v1.0/blogsite/user/delete/test"))
                .andExpect(status().isOk()); // Expect OK status
    }

    // ✅ Test for getUserBlogs
    @Test
    void getUserBlogs_success() throws Exception {
        // Prepare test blog object
        Blog blog = new Blog();
        blog.setTitle("userBlog");
        blog.setCategory("personal");

        // Mock the service method to return blogs by user ID 1
        when(blogService.getBlogsByUser(1L)).thenReturn(List.of(blog));

        // Perform GET request for user ID 1 and validate the response
        mockMvc.perform(get("/api/v1.0/blogsite/user/getalltest")
                        .param("userId", "1"))
                .andExpect(status().isOk()) // Expect OK status
                .andExpect(jsonPath("$[0].title").value("userBlog")); // Expect blog title in response
    }

    // ✅ Test for getBlogsByDuration
    @Test
    void getBlogsByDuration_success() throws Exception {
        // Prepare test BlogResponse object
        BlogResponse response = new BlogResponse();
        response.setTitle("durationBlog");
        response.setCategory("tech");

        // Mock the service method to return blogs by category and duration
        when(blogService.getBlogsByCategoryAndDuration("tech", "2023-01-01", "2023-12-31"))
                .thenReturn(List.of(response));

        // Perform GET request for blogs within duration and validate the response
        mockMvc.perform(get("/api/v1.0/blogsite/blogs/get/tech/2023-01-01/2023-12-31"))
                .andExpect(status().isOk()) // Expect OK status
                .andExpect(jsonPath("$[0].title").value("durationBlog")); // Expect title field in response
    }
}
