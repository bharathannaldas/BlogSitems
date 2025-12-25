package com.blogsite.dto;


import java.time.LocalDate;

public class BlogResponse {

    private String title;
    private String category;
    private String author;
    private LocalDate createdDate;

    public BlogResponse() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public BlogResponse(Builder builder) {
        this.title = builder.title;
        this.category = builder.category;
        this.author = builder.author;
        this.createdDate = builder.createdDate;
    }

    public static class Builder {
        private String title;
        private String category;
        private String author;
        private LocalDate createdDate;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder createdDate(LocalDate createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public BlogResponse build() {
            return new BlogResponse(this);
        }

    }
}

