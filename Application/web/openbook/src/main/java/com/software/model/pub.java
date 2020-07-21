package com.software.model;

import org.springframework.web.multipart.MultipartFile;

public class pub {

    public MultipartFile file;
    public MultipartFile image_file;
    public String title;
    public String description;
    public int category_id;


    public pub(MultipartFile file, MultipartFile image_file, String title, String description, int category) {
        this.file = file;
        this.image_file = image_file;
        this.title = title;
        this.description = description;
        this.category_id = category;
    }



    public MultipartFile getImage_file() {
        return image_file;
    }

    public void setImage_file(MultipartFile image_file) {
        this.image_file = image_file;
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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category) {
        this.category_id = category;
    }
}
