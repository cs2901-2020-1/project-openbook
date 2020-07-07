package com.software.repository;

import com.software.model.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Set;

public class LikeSpecification implements Specification<Likes> {
    private SearchCriteria criteria;

    public LikeSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Likes> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

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

            } else if( root.get(criteria.getKey()).getJavaType() == User.class) {
                // Like by User
                Join<Likes, User> profJoin = root.join(Likes_.user);

                return criteriaBuilder.equal(profJoin.get(User_.email), criteria.getValue());

            } else if( root.get(criteria.getKey()).getJavaType() == Publication.class) {
                // Like by Publication
                Join<Likes, Publication> profJoin = root.join(Likes_.publicationLike);

                return criteriaBuilder.equal(profJoin.get(Publication_.id), criteria.getValue());

            } else {
                return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
