package com.software.model;

public class com {


    public com(String text_comment, Long id_pub, Long id_comment_to) {
        this.text_comment = text_comment;
        this.id_pub = id_pub;
        this.id_comment_to = id_comment_to;
    }

    public com() {
    }

    private String text_comment;



    private Long id_pub;

    private Long id_comment_to;



    public Long getId_comment_to() {
        return id_comment_to;
    }

    public void setId_comment_to(Long id_comment_to) {
        this.id_comment_to = id_comment_to;
    }


    public String getText_comment() {
        return text_comment;
    }



    public void setText_comment(String text_comment) {
        this.text_comment = text_comment;
    }

    public Long getId_pub() {
        return id_pub;
    }

    public void setId_pub(Long id_pub) {
        this.id_pub = id_pub;
    }

}
