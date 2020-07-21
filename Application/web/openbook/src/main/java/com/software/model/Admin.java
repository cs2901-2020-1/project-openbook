package com.software.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "Admin")
@DiscriminatorValue("Admin")
public class Admin extends User {


    public Admin() {

    }

    public Admin(String email, String username, String password, String name, String surname) {
        super(email, username, password, name, surname);

    }

    @Override
    public String getTipo(){
        return "admin";
    }


}
