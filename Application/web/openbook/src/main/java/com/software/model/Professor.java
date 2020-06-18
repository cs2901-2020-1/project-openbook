package com.software.model;

import javax.persistence.*;

@Entity(name = "Professor")
@DiscriminatorValue("Professor")
public class Professor extends User{
    protected String titleId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "profe_publication",
            joinColumns =
                    { @JoinColumn(name = "professor_id", referencedColumnName = "email") },
            inverseJoinColumns =
                    { @JoinColumn(name = "publication_id", referencedColumnName = "id") })
    private Publication publication;

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


    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }
}
