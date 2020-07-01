package com.software.openbook;

import com.software.model.*;
import com.software.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.*;

@SpringBootApplication
@ComponentScan({"com.software"})
@EntityScan("com.software.model")
@EnableJpaRepositories("com.software.repository")
@EnableJpaAuditing
public class OpenbookApplication implements CommandLineRunner {

    @Autowired
    private AuthService authService;
//    @Autowired
//    private CategoryService catService;
//    @Autowired
//    private PublicationService publiService;
    @Autowired
    private CommentService commentService;
//    @Autowired
//    private TagService tagService;

    private static final Logger log = LoggerFactory.getLogger(OpenbookApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OpenbookApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Starting OpenBook ...");
    }

    public void test_subcomment(Long commentId) {
        String studentmail = "mit.mosquera@lamerced.edu.pe";
        Optional<User> optionalStudent = authService.getUser(studentmail);
        if (!optionalStudent.isPresent())
            return;

        Comment comment1 = new Comment("subcomentario 1", (float) 0.23, new Publication(),
                optionalStudent.get());
        Comment comment2 = new Comment("subcomentario 2", (float) 0.3, new Publication(),
                optionalStudent.get());
        commentService.addCommentToComment(commentId, comment1);
        commentService.addCommentToComment(commentId, comment2);
    }

    public void test_getsubcomment(Long parentcommentId) {
        List<Comment> comments = commentService.getSubcoments(parentcommentId);

        for(Comment comment: comments) {
            System.out.println(comment.getId()+" "+comment.getText_comment()+ " " + comment.getCreatedAt()
            +" from comment: " + comment.getParentComment().getText_comment());
        }
    }

}