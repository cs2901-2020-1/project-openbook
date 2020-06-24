package com.software.model;

import org.springframework.web.multipart.MultipartFile;

public class pub {


    public MultipartFile file;
    public String title;
    public String description;
    public String category;

    public pub(MultipartFile file, String title, String description, String category) {
        this.file = file;
        this.title = title;
        this.description = description;
        this.category = category;
    }


    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
