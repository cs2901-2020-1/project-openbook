package com.software.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "email")
    private User user;

    @ManyToOne
    @JoinColumn(name = "publication_id", referencedColumnName = "id")
    private Publication publicationLike;

    private Date likeDate;

    public Likes() {

    }

    public Likes(User user, Publication publication) {
        this.user = user;
        this.publicationLike = publication;
        this.likeDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Publication getPublicationLike() {
        return publicationLike;
    }

    public void setPublicationLike(Publication publicationLike) {
        this.publicationLike = publicationLike;
    }

    public Date getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(Date likeDate) {
        this.likeDate = likeDate;
    }
}
