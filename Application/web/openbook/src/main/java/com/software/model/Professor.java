package com.software.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "Professor")
@DiscriminatorValue("Professor")
public class Professor extends User{
    protected String titleId;

    public Professor() {

    }

    public Professor(String email, String username, String password, String name, String surname, String titleId) {
        super(email, username, password, name, surname);
        this.titleId = titleId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }
}
