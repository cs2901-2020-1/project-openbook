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

}
