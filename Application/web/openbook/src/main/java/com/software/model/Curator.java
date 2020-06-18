package com.software.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "Curator")
@DiscriminatorValue("Curator")
public class Curator extends User{
// publications_curated attribute missing
    public Curator() {

    }

    public Curator(String email, String username, String password, String name, String surname) {
        super(email, username, password, name, surname);
    }
}
