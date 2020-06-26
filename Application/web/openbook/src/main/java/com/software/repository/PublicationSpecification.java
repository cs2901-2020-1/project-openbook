package com.software.repository;

import com.software.model.*;
import com.software.model.Professor;
import com.software.model.Publication;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class PublicationSpecification implements Specification<Publication> {
    private SearchCriteria criteria;

    public PublicationSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Publication> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

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
            } else if( root.get(criteria.getKey()).getJavaType() == Professor.class) {

                Join<Publication, Professor> profJoin = root.join(Publication_.professor);

                return criteriaBuilder.equal(profJoin.get(Professor_.email), criteria.getValue());
            } else if( root.get(criteria.getKey()).getJavaType() == Category.class ) {

                Join<Publication, Category> categoryJoin = root.join(Publication_.category);

                return criteriaBuilder.equal(categoryJoin.get(Category_.id), criteria.getValue());
            } else {
                return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}