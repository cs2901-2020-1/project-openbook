package com.software.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Professor")
@DiscriminatorValue("Professor")
public class Professor extends User{
    protected String titleId;

    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY)
    private Set<Publication> publications = new HashSet<>();

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "photopath", length = 200)
    private String photoPath;

    public Professor() {

    }

    public Professor(String email, String username, String password, String name, String surname, String titleId) {
        super(email, username, password, name, surname);
        this.titleId = titleId;
    }

    @Override
    public String getTipo(){
        return "profesor";
    }

    public Set<Publication> getPublications() {
        return publications;
    }

    public void setPublications(Set<Publication> publications) {
        this.publications = publications;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
