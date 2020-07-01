package com.software.repository;

import com.software.model.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class TagSpecification implements Specification<Tag> {

    private SearchCriteria criteria;

    public TagSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

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

                SetJoin<Tag, Publication> profJoin = root.join(Tag_.publications);

                return criteriaBuilder.equal(profJoin.get(Publication_.id), criteria.getValue());
            }
        }
        return null;
    }
}
