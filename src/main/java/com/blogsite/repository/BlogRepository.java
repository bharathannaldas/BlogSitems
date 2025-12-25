package com.blogsite.repository;

import com.blogsite.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDate;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findByCategory(String category);

    List<Blog> findByUserId(Long userId);

    void deleteByTitle(String title);

    List<Blog> findByCategoryAndCreatedDateBetween(
            String category,
            LocalDate from,
            LocalDate to
    );
}




