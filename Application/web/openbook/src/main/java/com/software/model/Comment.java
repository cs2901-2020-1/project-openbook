package com.software.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Comment")
public class Comment {

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

    @ManyToOne
    private Comment parentComment;
}
