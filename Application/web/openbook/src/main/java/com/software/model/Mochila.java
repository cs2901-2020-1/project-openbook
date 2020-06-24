package com.software.model;


import javax.persistence.*;

@Entity
@Table(name = "Mochila")
public class Mochila {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;

    @ManyToOne
    @JoinColumn(name = "publication_id")
    private Publication publication_id;

    public Mochila(User user_id, Publication publication_id) {
        this.user_id = user_id;
        this.publication_id = publication_id;
    }

    public Mochila() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public Publication getPublication_id() {
        return publication_id;
    }

    public void setPublication_id(Publication publication_id) {
        this.publication_id = publication_id;
    }
}
