package com.blogsite.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class BlogDTO {

    @NotBlank @Size(min = 3, max = 100)
    private String title;

    @NotBlank
    private String category;

    @NotBlank
    private String content;

    private  UserDTO userDTO;


    public UserDTO getUserDTO() {
        return userDTO;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
