package com.software.service;
import com.software.model.*;

import com.software.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.jpa.domain.Specification.not;

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

    public Publication blockPublication(Long publicationId, String curator_id){
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
        publication.setEstado(3); // bloqueado
        publication.setRanking(0);

        updatePublication(publication);

        Publication publicationResult = publicationRepository.save(publication);

        return publicationResult;
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
        publication.setEstado(2); // verificado
        publication.setVerified(true);
        publication.setCurationDate(new Date());
        float ranking = publication.getRanking();
        ranking = ranking + 10000;
        publication.setRanking(ranking);
        updatePublication(publication);
        
        Publication publicationResult = publicationRepository.save(publication);

        return publicationResult;
    }

    public Publication savePublicationToCurate(Long publicationId, String curator_id) {
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
        publication.setEstado(1); // in process
        publication.setVerified(true);
        publication.setCurationDate(new Date());

        Publication publicationResult = publicationRepository.save(publication);

        return publicationResult;
    }

    public List<Publication> getPublicationsVerifiedByCurator(String curatorEmail) {
        // return all publications that has estado = 2
        PublicationSpecification specCuratorId = new PublicationSpecification(
                new SearchCriteria("curator", ":", curatorEmail));

        PublicationSpecification specEstado_2 = new PublicationSpecification(
                new SearchCriteria("estado", ":", 2));

        Specification<Publication> specification = Specification.where(specCuratorId).and(specEstado_2);

        return publicationRepository.findAll(specification,
                Sort.by(Sort.Direction.DESC, "ranking"));
    }

    public List<Publication> getPublicationsToVerifyByCurator(String curatorEmail) {
        // return all publications that has estado = 1
        PublicationSpecification specCuratorId = new PublicationSpecification(
                new SearchCriteria("curator", ":", curatorEmail));

        PublicationSpecification specEstado_1 = new PublicationSpecification(
                new SearchCriteria("estado", ":", 1));

        Specification<Publication> specification = Specification.where(specCuratorId).and(specEstado_1);

        return publicationRepository.findAll(specification,
                Sort.by(Sort.Direction.DESC, "ranking"));
    }

    public List<Publication> getPublicationsFromProfessor(String professorEmail) {

        PublicationSpecification specProfe = new PublicationSpecification(
                new SearchCriteria("professor", ":", professorEmail));

        PublicationSpecification specEstado_3 = new PublicationSpecification(
                new SearchCriteria("estado", ":", 3));

        Specification<Publication> specification = Specification.where(specProfe).and(
                not(specEstado_3));

        return publicationRepository.findAll(specification,
                Sort.by(Sort.Direction.DESC, "ranking"));
    }

    public List<Publication> getPublicationsFromCurator(String curatorEmail) {
        PublicationSpecification spec = new PublicationSpecification(
                new SearchCriteria("curator", ":", curatorEmail));

        return publicationRepository.findAll(spec,
                Sort.by(Sort.Direction.DESC, "ranking"));
    }

    public Page<Publication> getPublicationsFromCategory(int idCategory, int page, int number) {

        PublicationSpecification spec = new PublicationSpecification(
                new SearchCriteria("category", ":", idCategory));

        PublicationSpecification specEstado_3 = new PublicationSpecification(
                new SearchCriteria("estado", ":", 3));

        Specification<Publication> specification = Specification.where(spec).and(
                not(specEstado_3));

        return publicationRepository.findAll(specification,
                PageRequest.of(page, number, Sort.by(Sort.Direction.DESC,"ranking")));
        //List<Publication> publications = publicationRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "ranking"));
        //return new PageImpl<Publication>(publications, pageable, publications.size());
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

        Likes likeResult = like;
        List<Likes> likes = getLikeFromPublicationAndUser(publicationId,user_id);
        if(likes.size() == 0){
            likeResult = likeRepository.save(like);
            Set<Likes> userLike = user.getLike();
            userLike.add(likeResult);
            publication.getPublicationLike().add(likeResult);
            float ranking = publication.getRanking();
            ranking = ranking + 5;
            publication.setRanking(ranking);
            updatePublication(publication);
        } else {
            float ranking = publication.getRanking();
            ranking = ranking - 5;
            publication.setRanking(ranking);
            updatePublication(publication);
            likeRepository.delete(likes.get(0));
        }

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

    public Page<Publication> findPublicationByKeywords(String keywords, Pageable pageable) {
        String searchStr="";
        if (!keywords.equals("")) {
            searchStr = keywords.replaceAll(" ", ":*&");
            searchStr = searchStr + ":*";
        }

        return publicationRepository.findPublicationsFullTextSearchByDescription(searchStr, pageable);
    }

    public Page<Publication> getLastNPublications(int page, int number) {

        PublicationSpecification specEstado_3 = new PublicationSpecification(
                new SearchCriteria("estado", ":", 3));

        Specification<Publication> specification = Specification.not(specEstado_3);

        return publicationRepository.findAll(specification, PageRequest.of(page, number,
                Sort.by(Sort.Direction.DESC,"createdAt")));
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

    public List<IPublicationLikeCount> publicationLikeCountsByProfessor(String professor_id) {
        return publicationRepository.countTotalPublicationByLikeInterface(professor_id);
    }

    public List<IDatePublicationCount> datePublicationsCountsByProfessor(String professor_id) {
        return publicationRepository.countTotalPublicationByDateInterface(professor_id);
    }


    public Page<Publication> getTopNPublications(int page, int number) {
        return publicationRepository.findAll(PageRequest.of(page, number,
                Sort.by(Sort.Direction.DESC,"ranking")));
    }

    public boolean verify_content(String resourcePath, User user) {
        List<Publication> publications = getPublicationsFromProfessor(user.getEmail());
        for(Publication publication: publications){
            if(resourcePath.equals(publication.getResource_path())){
                return true;
            }
        }
        return false;
    }
}