package com.blogsite.controller;

import com.blogsite.dto.BlogDTO;
import com.blogsite.dto.BlogResponse;
import com.blogsite.model.Blog;
import com.blogsite.serivce.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/blogsite")
public class BlogController {

    private final BlogService service;
    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    public BlogController(BlogService service) {
        this.service = service;
    }

    @PostMapping("/user/blogs/add/{blogname}")
    public Blog addBlog(@PathVariable String blogname,
                        @RequestBody BlogDTO blogRequet) {
        logger.info("Adding new blog with name: {}", blogname);
        blogRequet.setTitle(blogname);
        return service.addBlog(blogRequet);
    }

    @GetMapping("/blogs/info/{category}")
    public List<Blog> getBlogsByCategory(@PathVariable String category) {
        logger.info("getBlogsByCategory: {}", category);
        return service.getBlogsByCategory(category);
    }

    @GetMapping("/user/getalltest")
    public List<Blog> getUserBlogs(@RequestParam Long userId) {
        logger.info("getUserBlogs: {}", userId);
        return service.getBlogsByUser(userId);
    }
    // GET ALL BLOGS (NO USER FILTER)
    @GetMapping("/user/getall")
    public List<Blog> getAllBlogs() {
        return service.getAllBlogs();
    }


    @DeleteMapping("/user/delete/{blogname}")
    public void deleteBlog(@PathVariable String blogname) {
        logger.info("Delete new blog with name: {}", blogname);
        service.deleteByName(blogname);
    }

    @GetMapping("/blogs/get/{category}/{from}/{to}")
    public List<BlogResponse> getBlogsByDuration(
            @PathVariable String category,
            @PathVariable String from,
            @PathVariable String to) {
        logger.info("Get blogs by dates: {}", category);
        return service.getBlogsByCategoryAndDuration(category, from, to);
    }
}
