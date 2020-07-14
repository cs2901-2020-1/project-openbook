package com.software.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "description",length = 10)
    private String description;

    @Column(name = "name",length = 100)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Publication> publication;

    public Category() {

    }

    public Category(String description) {

        this.description = description;
        int id = Integer.parseInt(description);
        if(id<=4){
            this.id = id;this.name = "Matemáticas";
        }
        else{
            this.id = id;this.name = "Ciencia y Tecnología";
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
