package com.software.service;
/*
import com.software.model.Publication;
import com.software.repository.PublicationRepository;
import com.software.repository.PublicationSpecification;
import com.software.repository.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;

    public void addPublication(Publication publication) {
        publicationRepository.save(publication);
    }

    public List<Publication> getPublicationFromProfessor(String professorEmail) {

        PublicationSpecification spec = new PublicationSpecification(
                new SearchCriteria("professor", ":", professorEmail));

        return publicationRepository.findAll(spec);
    }
}
*/