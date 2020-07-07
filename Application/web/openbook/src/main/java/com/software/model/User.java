package com.software.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "USERS")
@DiscriminatorColumn(name = "user_type")
public class User {

    @Id
    @Column(name = "email",length = 30)
    private String email;

    @Column(name = "username", length = 15)
    private String username;

    @Column(name = "password", length = 15)
    private String password;

    @Column(name = "name", length = 40)
    private String name;

    @Column(name = "surname", length = 70)
    private String surname;

    @Column(name = "status", length = 15)
    private String status;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Likes> like = new HashSet<>();

    public User(){

    }

    public User(String email, String username, String password, String name, String surname) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.status = "Active";
    }
    public String getTipo() {
        return null;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Likes> getLike() {
        return like;
    }

    public void setLike(Set<Likes> like) {
        this.like = like;
    }
}
