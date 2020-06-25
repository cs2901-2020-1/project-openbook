package com.software.repository;

import com.software.model.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class CommentSpecification implements Specification<Comment> {
    private SearchCriteria criteria;

    public CommentSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return criteriaBuilder.greaterThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return criteriaBuilder.lessThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");

            } else if( root.get(criteria.getKey()).getJavaType() == Publication.class) {

                Join<Comment, Publication> commentPubJoin = root.join(Comment_.publication);

                return criteriaBuilder.equal(commentPubJoin.get(Publication_.id), criteria.getValue());
            } else if( root.get(criteria.getKey()).getJavaType() == Comment.class) {

                Join<Comment, Comment> parentCommentJoin = root.join(Comment_.parentComment);

                return criteriaBuilder.equal(parentCommentJoin.get(Comment_.id), criteria.getValue());
            } else {
                return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
