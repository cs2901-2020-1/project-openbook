package com.software.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Publication")
public class Publication extends AuditModel {

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



    @Column(name = "image_path",length = 200)
    private String image_path;




    //@ManyToOne(fetch = FetchType.LAZY) //Don't retrieve the object
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "publication", fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    //Tags
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "publications_tags",
            joinColumns = { @JoinColumn(name = "publication_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    private Set<Tag> manyTags = new HashSet<>();

    private Boolean verified;

    @ManyToOne
    @JoinColumn(name = "curator_id")
    private Curator curator;

    @Temporal(TemporalType.TIMESTAMP)
    private Date curationDate = null;

    @OneToMany(mappedBy = "publicationLike")
    private List<Likes> publicationLike;

    @Column(name = "visits")
    private long visits = 0;

    public Publication(String title, String description, float ranking, String resource_path, Professor professor,
                       Category category) {
        this.title = title;
        this.description = description;
        this.ranking = ranking;
        this.resource_path = resource_path;
        this.professor = professor;
        this.category = category;
        this.verified = false;
    }

    public Publication() {
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Tag> getManyTags() {
        return manyTags;
    }

    public void setManyTags(Set<Tag> manyTags) {
        this.manyTags = manyTags;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public void setCurator(Curator curator) {
        this.curator = curator;
    }

    public void setCurationDate(Date curationDate) {
        this.curationDate = curationDate;
    }

    public Boolean getVerified() {
        return verified;
    }

    public Curator getCurator() {
        return curator;
    }

    public Date getCurationDate() {
        return curationDate;
    }

    public List<Likes> getPublicationLike() {
        return publicationLike;
    }

    public void setPublicationLike(List<Likes> publicationLike) {
        this.publicationLike = publicationLike;
    }

    public long getVisits() {
        return visits;
    }

    public void setVisits(long visits) {
        this.visits = visits;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "id=" + this.id +
                ", title=" + this.title +
                ", description='" + this.description + '\'' +
                '}';
    }
}
