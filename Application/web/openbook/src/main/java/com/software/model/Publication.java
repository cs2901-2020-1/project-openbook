package com.software.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "Publication")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "title",length = 50)
    private String title;

    @Column(name = "description",length = 500)
    private String description;


    @Column(name = "ranking", length = 10, precision=4)
    private float ranking;

    @Column(name = "resource_path",length = 200)
    private String resource_path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Professor professor;


    //private Curator curator; /missing
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;





    public Publication(String title, String description, float ranking, String resource_path, Professor professor) {
        this.title = title;
        this.description = description;
        this.ranking = ranking;
        this.resource_path = resource_path;
        this.professor = professor;
    }

    public Publication() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRanking() {
        return ranking;
    }

    public void setRanking(float ranking) {
        this.ranking = ranking;
    }

    public String getResource_path() {
        return resource_path;
    }

    public void setResource_path(String resource_path) {
        this.resource_path = resource_path;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
