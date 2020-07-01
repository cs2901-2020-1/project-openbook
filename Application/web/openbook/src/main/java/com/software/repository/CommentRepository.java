package com.software.repository;

import com.software.model.Comment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long>,
        JpaSpecificationExecutor<Comment> {
}
