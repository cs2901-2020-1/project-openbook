package com.software.model;

import org.springframework.web.multipart.MultipartFile;

public class prof {



    public String description;

    public prof(String description, MultipartFile photoFile) {
        this.description = description;
        this.photoFile = photoFile;
    }

    public MultipartFile photoFile;





    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(MultipartFile photoFile) {
        this.photoFile = photoFile;
    }


}
