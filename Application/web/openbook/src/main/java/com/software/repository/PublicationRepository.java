package com.software.repository;

import com.software.model.Publication;
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
}
