package com.software.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Curator")
@DiscriminatorValue("Curator")
public class Curator extends User{

    @OneToMany(mappedBy = "curator", fetch = FetchType.LAZY)
    private Set<Publication> publicationsCurated = new HashSet<>();

    public Curator() {

    }

    public Curator(String email, String username, String password, String name, String surname) {
        super(email, username, password, name, surname);
    }

    public Set<Publication> getPublicationsCurated() {
        return publicationsCurated;
    }

    public void setPublicationsCurated(Set<Publication> publicationsCurated) {
        this.publicationsCurated = publicationsCurated;
    }
}
