package com.software.service;

import com.software.model.Comment;
import com.software.model.Publication;
import com.software.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PublicationRepository publicationRepository;

    public Comment addCommentToPublication(long publicationId, Comment comment) {

        Set<Comment> comments = new HashSet<>();
        Publication publication_1 = new Publication();
        Optional<Publication> publicationOpt = publicationRepository.findById(publicationId);
        if (!publicationOpt.isPresent()) {
            //throw new ResourceNotFoundException("Professor " + professor_id + "does not exists.");
            System.out.println("Publication " + publicationId + " does not exists.");
        }
        Publication publication = publicationOpt.get();

        comment.setPublication(publication);

        Comment commentResult = commentRepository.save(comment);

        publicationRepository.save(publication);
        comments.add(commentResult);
        publication_1.setComments(comments);

        return commentResult;
    }

    public Comment addCommentToComment(long commentId, Comment comment) {
        Set<Comment> comments = new HashSet<>();
        Comment comment_1 = new Comment();
        Optional<Comment> parentCommentOpt = commentRepository.findById(commentId);
        if (!parentCommentOpt.isPresent()) {
            //throw new ResourceNotFoundException("Professor " + professor_id + "does not exists.");
            System.out.println("Parent comment  " + commentId + " does not exists.");
        }
        Comment parentComment = parentCommentOpt.get();

        Optional<Publication> publicationOpt = publicationRepository.findById(
                parentComment.getPublication().getId());
        if (!publicationOpt.isPresent()) {
            //throw new ResourceNotFoundException("Professor " + professor_id + "does not exists.");
            System.out.println("Publication " + parentComment.getPublication().getId() + " does not exists.");
        }
        Publication publication = publicationOpt.get();

        comment.setParentComment(parentComment);
        comment.setPublication(publication);

        Comment commentResult = commentRepository.save(comment);

        commentRepository.save(parentComment);
        comments.add(commentResult);
        comment_1.setSubComments(comments);

        return commentResult;
    }

    public List<Comment> getCommentsPublication(long idPublication) {
        CommentSpecification spec = new CommentSpecification(
                new SearchCriteria("publication", ":", idPublication));
        return commentRepository.findAll(spec);
    }

    public List<Comment> getSubcoments(long idParentComment) {
        CommentSpecification spec = new CommentSpecification(
                new SearchCriteria("parentComment", ":", idParentComment));
        return commentRepository.findAll(spec);
    }

    public Optional<Comment> getComment(long commentId) {
        return commentRepository.findById(commentId);
    }
}
