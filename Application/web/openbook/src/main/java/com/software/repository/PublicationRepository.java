package com.software.repository;

import com.software.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface PublicationRepository extends JpaRepository<Publication, Long>,
        JpaSpecificationExecutor<Publication> {
}
