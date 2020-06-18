package com.software.model;

import javax.persistence.*;

@Entity
@Table(name = "Publication")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "type",length = 30)
    private String type;

    @Column(name = "name",length = 50)
    private String name;

    @Column(name = "ranking", length = 10, precision=4)
    private float ranking;

    @Column(name = "resource_path",length = 200)
    private String resource_path;

    @OneToOne(mappedBy = "publication")
    private Professor professor;

    //@OneToOne(cascade = CascadeType.ALL)
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    public Publication() {

    }

    public Publication(String type, String name, float ranking, String resource_path,
                       Professor professor, Category category) {
        this.type = type;
        this.name = name;
        this.ranking = ranking;
        this.resource_path = resource_path;
        this.professor = professor;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
