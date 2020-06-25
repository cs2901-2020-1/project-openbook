package com.software.service;
import com.software.model.Professor;

import com.software.model.Publication;
import com.software.model.User;
import com.software.repository.PublicationRepository;
import com.software.repository.PublicationSpecification;
import com.software.repository.SearchCriteria;
import com.software.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PublicationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PublicationRepository publicationRepository;

    public Publication addPublication(Publication publication, String professor_id) {
        Set<Publication> publications = new HashSet<>();
        Professor professor_1 = new Professor();
        Optional<User> professorOpt = userRepository.findById(professor_id);
        if (!professorOpt.isPresent()) {
            //throw new ResourceNotFoundException("Professor " + professor_id + "does not exists.");
            System.out.println("Professor " + professor_id + " does not exists.");
        }
        Professor professor = (Professor) professorOpt.get();

        publication.setProfessor(professor);

        Publication publicationResult = publicationRepository.save(publication);

        //publicationRepository.save(publication);
        publications.add(publicationResult);
        professor_1.setPublications(publications);

        return publicationResult;
    }

    public List<Publication> getPublicationsFromProfessor(String professorEmail) {

        PublicationSpecification spec = new PublicationSpecification(
                new SearchCriteria("professor", ":", professorEmail));

        return publicationRepository.findAll(spec);
    }

    public List<Publication> getPublicationsFromCategory(int idCategory) {

        PublicationSpecification spec = new PublicationSpecification(
                new SearchCriteria("category", ":", idCategory));

        return publicationRepository.findAll(spec);
    }

    //ToDo Get publications from tags

    public Optional<Publication> getPublication(long publicationId) {
        return publicationRepository.findById(publicationId);
    }
}