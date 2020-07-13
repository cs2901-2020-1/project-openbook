package com.software.repository;

import com.software.model.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PublicationRepository extends JpaRepository<Publication, Long>,
        JpaSpecificationExecutor<Publication> {

    List<Publication> findByManyTags_Name(String name);

    @Query("SELECT b FROM Publication b LEFT JOIN FETCH b.publicationLike WHERE b.id = :id")
    Optional<Publication> findCompletePublication(@Param("id") Long id);

    @Query(value = "SELECT pub2.* FROM Publication pub2" +
            " WHERE pub2.id IN ( SELECT p_search.id FROM " +
            "( SELECT  pu.id, " +
            "        to_tsvector(pu.description) || " +
            "        to_tsvector(cat.description) ||" +
            "        to_tsvector(pro.name) ||" +
            "        to_tsvector(pro.surname) as document " +
            "from Publication pu " +
            "inner join Category cat " +
            "ON cat.id = pu.category_id " +
            "INNER JOIN Users pro " +
            "ON pro.email = pu.professor_id " +
            "AND pro.user_type = 'Professor' ) p_search " +
            "WHERE p_search.document @@ to_tsquery(:query)) " +
            "ORDER BY pub2.ranking DESC",
            countQuery= "SELECT count(pub2.id) FROM publication pub2 " +
                    "where pub2.id IN ( SELECT p_search.id FROM " +
                    "( SELECT  pu.id,\n" +
                    "        to_tsvector(pu.description) || " +
                    "        to_tsvector(cat.description) || " +
                    "        to_tsvector(pro.name) ||\n" +
                    "        to_tsvector(pro.surname) as document " +
                    "from publication pu " +
                    "inner join category cat " +
                    "ON cat.id = pu.category_id " +
                    "INNER JOIN users pro\n" +
                    "ON pro.email = pu.professor_id " +
                    "AND pro.user_type = 'Professor' ) p_search " +
                    "WHERE p_search.document @@ to_tsquery(:query))",
            nativeQuery = true)
    //List<Publication> findPublicationsFullTextSearchByDescription(@Param("query") String query);
    Page<Publication> findPublicationsFullTextSearchByDescription(@Param("query") String query, Pageable pageable);
}
