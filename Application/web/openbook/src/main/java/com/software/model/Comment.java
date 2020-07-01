package com.software.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Comment")
public class Comment extends AuditModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "text_comment",length = 1000)
    private String text_comment;

    @Column(name = "valoration", length = 10, precision=4)
    private float valoration;

    @OneToMany(mappedBy="parentComment")
    private Set<Comment> subComments;

    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ManyToOne(optional = false)
    @JoinColumn(name = "publication_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Publication publication;

    @ManyToOne
    private Comment parentComment;

    @OneToOne
    private User commentAuthor;

    public Comment(String text_comment, float valoration, Publication publication, User commentAuthor) {
        this.text_comment = text_comment;
        this.valoration = valoration;
        this.publication = publication;
        this.commentAuthor = commentAuthor;
    }

    public Comment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText_comment() {
        return text_comment;
    }

    public void setText_comment(String text_comment) {
        this.text_comment = text_comment;
    }

    public float getValoration() {
        return valoration;
    }

    public void setValoration(float valoration) {
        this.valoration = valoration;
    }

    public Set<Comment> getSubComments() {
        return subComments;
    }

    public void setSubComments(Set<Comment> subComments) {
        this.subComments = subComments;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + this.id +
                ", text_comment=" + this.text_comment +
                ", valoration='" + this.valoration + '\'' +
                ", publication=" + this.publication +
                '}';
    }
}
