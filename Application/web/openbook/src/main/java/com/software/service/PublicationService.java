package com.software.service;
import com.software.model.*;

import com.software.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PublicationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private LikeRepository likeRepository;

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

        publications.add(publicationResult);
        professor_1.setPublications(publications);

        return publicationResult;
    }

    public Publication updatePublication(Publication publication) {

        return publicationRepository.save(publication);
    }

    public Publication curatePublication(Long publicationId, String curator_id) {
        Optional<User> curatorOpt = userRepository.findById(curator_id);

        if (!curatorOpt.isPresent()) {
            //throw new ResourceNotFoundException("Professor " + professor_id + "does not exists.");
            System.out.println("Curator " + curator_id + " does not exists.");
        }

        Curator curator = (Curator) curatorOpt.get();

        Optional<Publication> publicationOpt = this.getPublication(publicationId);

        if (!publicationOpt.isPresent()) {
            System.out.println("Publicatior " + publicationId + " does not exists.");
        }

        Publication publication = publicationOpt.get();
        publication.setCurator(curator);
        publication.setVerified(true);
        publication.setCurationDate(new Date());
        
        Publication publicationResult = publicationRepository.save(publication);

        return publicationResult;
    }

    public List<Publication> getPublicationsFromProfessor(String professorEmail) {

        PublicationSpecification spec = new PublicationSpecification(
                new SearchCriteria("professor", ":", professorEmail));

        return publicationRepository.findAll(spec,
                Sort.by(Sort.Direction.DESC, "ranking"));
    }

    public List<Publication> getPublicationsFromCurator(String curatorEmail) {
        PublicationSpecification spec = new PublicationSpecification(
                new SearchCriteria("curator", ":", curatorEmail));

        return publicationRepository.findAll(spec,
                Sort.by(Sort.Direction.DESC, "ranking"));
    }

    public List<Publication> getPublicationsFromCategory(int idCategory) {

        PublicationSpecification spec = new PublicationSpecification(
                new SearchCriteria("category", ":", idCategory));

        return publicationRepository.findAll(spec,
                Sort.by(Sort.Direction.DESC, "ranking"));
    }

    public List<Publication> getPublicationsfromTagName(String tagName) {
        //return publicationRepository.findByManyTags_Name(name);
        PublicationSpecification spec = new PublicationSpecification(
                new SearchCriteria("manyTags", ":", tagName));

        return publicationRepository.findAll(spec,
                Sort.by(Sort.Direction.DESC, "ranking"));
    }

    public Optional<Publication> getPublication(long publicationId) {
        return publicationRepository.findById(publicationId);
    }

    //Method to retrieve Like information
    public Optional<Publication> getPublicationWithLikes(long publicationId) {
        return publicationRepository.findCompletePublication(publicationId);
    }

    public Likes likePublication(Long publicationId, String user_id) {
        //Optional<User> userOptional = userRepository.findById(user_id);
        //Call method to retrieve Like information
        Optional<User> userOptional = userRepository.findCompleteUser(user_id);
        Likes like;

        if (!userOptional.isPresent()) {
            System.out.println("User " + user_id + " does not exists.");
        }

        User user = userOptional.get();

        //Optional<Publication> publicationOpt = this.getPublication(publicationId);
        //Call method to retrieve Like information
        Optional<Publication> publicationOpt = this.getPublicationWithLikes(publicationId);

        if (!publicationOpt.isPresent()) {
            System.out.println("Publication " + publicationId + " does not exists.");
        }

        Publication publication = publicationOpt.get();

        like = new Likes(user, publication);

        Likes likeResult = likeRepository.save(like);
        Set<Likes> userLike = user.getLike();
        userLike.add(likeResult);
        publication.getPublicationLike().add(likeResult);

        return likeResult;
    }

    public List<Likes> getLikeFromPublicationAndUser(Long publicationId, String user_id) {
        LikeSpecification specUser = new LikeSpecification(
                new SearchCriteria("user", ":", user_id));
        LikeSpecification specPub = new LikeSpecification(
                new SearchCriteria("publicationLike", ":", publicationId));

        Specification<Likes> specification = Specification.where(specUser).and(specPub);

        return likeRepository.findAll(specification);
    }

    public List<Likes> getLikesFromPublication(Long publicationId) {

        LikeSpecification specPub = new LikeSpecification(
                new SearchCriteria("publicationLike", ":", publicationId));

        return likeRepository.findAll(specPub);
    }

    public List<Likes> getLikesFromUser(String user_id) {

        LikeSpecification specUser = new LikeSpecification(
                new SearchCriteria("user", ":", user_id));

        return likeRepository.findAll(specUser);
    }
}