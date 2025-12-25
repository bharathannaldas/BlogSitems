package com.blogsite.serivce;

import com.blogsite.dto.BlogDTO;
import com.blogsite.dto.BlogResponse;
import com.blogsite.exception.BlogNotFoundException;  // Import custom exception
import com.blogsite.model.Blog;
import com.blogsite.model.User;
import com.blogsite.repository.BlogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final BlogRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(BlogService.class);

    public BlogService(BlogRepository repo) {
        this.repo = repo;
    }

    // INSERT
    public Blog addBlog(BlogDTO blogRequest) {
        try {
            logger.info("Adding a new blog with title: {}", blogRequest.getTitle());
            Blog blog =new Blog();
            User user =new User();
            blog.setCategory(blogRequest.getCategory());
            blog.setContent(blogRequest.getContent());
            blog.setTitle(blogRequest.getTitle());
            if (blogRequest.getUserDTO() != null) {
                user.setId(blogRequest.getUserDTO().getId());
                blog.setUser(user);
            }
            blog.setStatus("PENDING");
            blog.setCreatedDate(LocalDate.now());
            return repo.save(blog);
        } catch (Exception ex) {
            logger.error("Error adding blog with title: {}", blogRequest.getTitle(), ex);
            throw new RuntimeException("Error adding the blog.");
        }
    }

    // SELECT by Category
    public List<Blog> getBlogsByCategory(String category) {
        try {
            logger.info("Fetching blogs by category: {}", category);
            return repo.findByCategory(category);
        } catch (Exception ex) {
            logger.error("Error fetching blogs by category: {}", category, ex);
            throw new RuntimeException("Error fetching blogs by category.");
        }
    }

    // SELECT by User ID
    public List<Blog> getBlogsByUser(Long userId) {
        try {
            logger.info("Fetching blogs by user: {}", userId);
            return repo.findByUserId(userId);
        } catch (Exception ex) {
            logger.error("Error fetching blogs by user: {}", userId, ex);
            throw new RuntimeException("Error fetching blogs by user.");
        }
    }

    // GET ALL BLOGS
    public List<Blog> getAllBlogs() {
        try {
            logger.info("Fetching all blogs");
            return repo.findAll();
        } catch (Exception ex) {
            logger.error("Error fetching all blogs", ex);
            throw new RuntimeException("Error fetching all blogs.");
        }
    }

    // DELETE (SAFE)
    public void deleteByName(String blogName) {
        if (blogName == null) {
            throw new IllegalArgumentException("Blog title cannot be null");
        }

        try {
            logger.warn("Attempting to delete blog with title: {}", blogName);

            // Assuming the method repo.deleteByTitle throws BlogNotFoundException if no blog is found
            repo.deleteByTitle(blogName);

            logger.info("Successfully deleted blog with title: {}", blogName);
        } catch (BlogNotFoundException ex) {
            logger.error("Blog not found during delete operation: {}", blogName, ex);
            throw new BlogNotFoundException("Blog not found with title: " + blogName);  // Update exception message
        } catch (Exception ex) {
            logger.error("Error deleting blog with title: {}", blogName, ex);
            throw new RuntimeException("Error deleting the blog.");
        }
    }



    // UPDATE (SAFE)
    @Transactional
    public Blog updateBlog(Long blogId, String content, String category) {
        try {
            logger.info("Updating blog with ID: {}. New content: {}, New category: {}", blogId, content, category);
            Blog blog = repo.findById(blogId)
                    .orElseThrow(() -> new BlogNotFoundException("Blog not found with ID: " + blogId));
            blog.setContent(content);
            blog.setCategory(category);
            return repo.save(blog);
        } catch (BlogNotFoundException ex) {
            logger.error("Blog not found for update: {}", blogId, ex);
            throw ex; // Rethrow the exception to be handled by @ControllerAdvice
        } catch (Exception ex) {
            logger.error("Error updating blog with ID: {}", blogId, ex);
            throw new RuntimeException("Error updating the blog.");
        }
    }

    // SELECT WITH BUILDER PATTERN (WITH DATE RANGE)
    public List<BlogResponse> getBlogsByCategoryAndDuration(
            String category,
            String from,
            String to) {

        try {
            logger.info("Fetching blogs for category: {} between dates {} and {}", category, from, to);
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);

            return repo.findByCategoryAndCreatedDateBetween(category, fromDate, toDate)
                    .stream()
                    .map(blog -> new BlogResponse.Builder()
                            .title(blog.getTitle())
                            .category(blog.getCategory())
                            .author(blog.getUser().getUsername())
                            .createdDate(blog.getCreatedDate())
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Error fetching blogs by category and duration: {} to {}", from, to, ex);
            throw new RuntimeException("Error fetching blogs by category and duration.");
        }
    }
}
