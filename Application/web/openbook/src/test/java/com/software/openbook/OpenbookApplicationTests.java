package com.software.openbook;

import com.software.model.*;
import com.software.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class OpenbookApplicationTests {
    @Autowired
    private AuthService authService;
    @Autowired
    private CategoryService catService;
    @Autowired
    private TagService tagService;
    @Autowired
    private PublicationService publiService;
    @Autowired
    private CommentService commentService;

    @Test
    void contextLoads() {
    }

//    @Test
//    public void students_registration() {
//        String email1 = "osman.vilchez@utec.edu.pe";
//        String email2 = "victor.ascunia@lamerced.edu.pe";
//        Student student1 = new Student(email1,"osoman","123456",
//                "Osman", "Vilchez", "Cole 1", 9);
//        Student student2 = new Student(email2, "vic2020", "12345",
//                "Víctor","Ascuña", "La Cole 2", 9);
//        authService.addUser(student1);
//        authService.addUser(student2);
//
//        Optional<User> studentOptional1 = authService.getUser(email1);
//
//        assertEquals("Student with " + email1 + "not registered", true,
//                studentOptional1.isPresent());
//
//        Optional<User> studentOptional2 = authService.getUser(email1);
//
//        assertEquals("Student with " + email2 + "not registered", true,
//                studentOptional2.isPresent());
//    }
//
//    @Test
//    public void professor_registration() {
//        String profEmail1 = "yamilet@utec.edu.pe";
//        String profEmail2 = "jbellido@utec.edu.pe";
//
//        Professor profe1 = new Professor(profEmail1, "yami", "654321", "Yamilet",
//                "Serrano", "Phd. Matematicas");
//        Professor profe2 = new Professor(profEmail2, "jbellido", "3141516", "Jesús",
//                "Bellido", "Phd. Computer Science");
//
//        authService.addUser(profe1);
//        authService.addUser(profe2);
//
//        Optional<User> professorOptional1 = authService.getUser(profEmail1);
//
//        assertEquals("Professor with " + profEmail1 + "not registered", true,
//                professorOptional1.isPresent());
//
//        Optional<User> professorOptional2 = authService.getUser(profEmail2);
//
//        assertEquals("Professor with " + profEmail2 + "not registered", true,
//                professorOptional2.isPresent());
//    }
//
//    @Test
//    public void curator_registration() {
//        String curatorEmail1 = "curator1@utec.edu.pe";
//        String curatorEmail2 = "curator2@utec.edu.pe";
//
//        Curator curator1 = new Curator(curatorEmail1, "curator1", "654321", "Name 1",
//                "Surname 21");
//        Curator curator2 = new Curator(curatorEmail2, "curator2", "654321", "Name 12",
//                "Surname 22");
//
//        authService.addUser(curator1);
//        authService.addUser(curator2);
//
//        Optional<User> curatorOptional1 = authService.getUser(curatorEmail1);
//
//        assertEquals("Curator with " + curatorEmail1 + "not registered", true,
//                curatorOptional1.isPresent());
//
//        Optional<User> curatorOptional2 = authService.getUser(curatorEmail2);
//
//        assertEquals("Curator with " + curatorEmail2 + "not registered", true,
//                curatorOptional2.isPresent());
//    }
//
//
//    @Test
//    public void savePublications_and_retrieveByTag() {
//        //Adding categories
//        Category cat1 = new Category("Algebra");
//        Category cat2 = new Category("Polinomios");
//        Category cat3 = new Category("Conteo y Permutaciones");
//
//        catService.addCategory(cat1);
//        catService.addCategory(cat2);
//        catService.addCategory(cat3);
//
//        Set<Tag> tagslist = new HashSet<>();
//        String tagName1 = "curso_2020";
//        String tagName2 = "matematicas";
//        String emailProfessor = "yamilet@utec.edu.pe";
//        int idCategory = 2;
//        int n = 100;
//
//        Tag tag1 = new Tag(tagName1);
//        Tag tag2 = new Tag(tagName2);
//
//        //Adding publication
//        Optional<User> optionalProfessor = authService.getUser(emailProfessor);
//        assertEquals("Professor "+emailProfessor+" not registered",
//                true, optionalProfessor.isPresent());
//
//        Optional<Category> optionalCategory = catService.getCategory(idCategory);
//
//        assertEquals("Category with id  "+ idCategory + " not registered",
//                true, optionalCategory.isPresent());
//
//        Publication posts[] = new Publication[n];
//
//        for(int i = 0; i < n; i++) {
//            posts[i] = new Publication("PDF", "Manual mate parte " + i + " con tags",
//                    0, "\\server\\folder\\test"+i+".pdf",
//                    (Professor) optionalProfessor.get(), optionalCategory.get());
//
//            tag1.getPublications().add(posts[i]);
//            tag2.getPublications().add(posts[i]);
//        }
//
//        tagslist.add(tag1);
//        tagslist.add(tag2);
//
//        for(int i = 0; i < n; i++) {
//            posts[i].setManyTags(tagslist);
//        }
//
//        //post.setManyTags(Collections.singleton(tag1));
//        //post.setManyTags(Collections.singleton(tag2));
//        for(int i = 0; i < n; i++) {
//            publiService.addPublication(posts[i], emailProfessor);
//        }
//
//        List<Publication> pubs = publiService.getPublicationsfromTagName(tagName1);
//
//        assertEquals("Publications number with tag " + tagName1 + " incorrect",
//                n, pubs.size());
//    }
//
//    @Test
//    public void saveComments_and_retrieveByPublicationId() {
//        String email1 = "osman.vilchez@utec.edu.pe";
//        long publicationId = 100;
//        int cantComments = 1100;
//        Optional<User> optionalStudent = authService.getUser(email1);
//
//        assertEquals("Student "+email1+" not registered",
//                true, optionalStudent.isPresent());
//
//        for (int i = 0; i < cantComments ; i++) {
//            int num = i+1;
//            Comment comment = new Comment("Comentario nro " + num, 1, new Publication(),
//                    optionalStudent.get());
//            commentService.addCommentToPublication(publicationId, comment);
//        }
//
//        List<Comment> commentsdb = commentService.getCommentsPublication(publicationId);
//
//        assertEquals("Coments number for publication " + publicationId + " incorrect",
//                cantComments, commentsdb.size());
//
//    }
//
//    @Test
//    public void curate_publications() {
//        String curatorEmail1 = "curator1@utec.edu.pe";
//        long publicationId = 101;
//
//        Optional<User> curatorOptional1 = authService.getUser(curatorEmail1);
//
//        assertEquals("Curator with " + curatorEmail1 + "not registered", true,
//                curatorOptional1.isPresent());
//
//        Optional<Publication> pub = publiService.getPublication(publicationId);
//
//        assertEquals("Publication id " + publicationId + "not registered", true,
//                pub.isPresent());
//
//        Publication publication = pub.get();
//
//        assertEquals("Publication id " + publicationId + "is already curated!", false,
//                publication.getVerified());
//
//        Publication pub2 = publiService.curatePublication(publicationId, curatorEmail1);
//
//        Optional<Publication> pub3 = publiService.getPublication(publicationId);
//
//        assertEquals("Publication id " + publicationId + "wasn't curated!", true,
//                pub3.get().getVerified());
//    }
//
//    @Test
//    public void giveLikesToPublication() {
//        String studentEmail1 = "victor.ascunia@lamerced.edu.pe";
//        String studentEmail2 = "osman.vilchez@utec.edu.pe";
//
//        long publicationId = 101;
//
//        Optional<User> userOptional1 = authService.getUser(studentEmail1);
//        Optional<User> userOptional2 = authService.getUser(studentEmail2);
//
//        assertEquals("Student " + studentEmail1 + "not registered", true,
//                userOptional1.isPresent());
//
//        assertEquals("Student " + studentEmail2 + "not registered", true,
//                userOptional2.isPresent());
//
//        Optional<Publication> pub = publiService.getPublication(publicationId);
//
//        assertEquals("Publication id " + publicationId + "not registered", true,
//                pub.isPresent());
//
//        List<Likes> likesList;
//        likesList = publiService.getLikeFromPublicationAndUser(publicationId, studentEmail1);
//
//        assertEquals("Student " + studentEmail1 + " already like publication " +
//                publicationId , 0, likesList.size());
//
//        likesList = publiService.getLikeFromPublicationAndUser(publicationId, studentEmail2);
//
//        assertEquals("Student " + studentEmail2 + " already like publication " +
//                publicationId , 0, likesList.size());
//
//        publiService.likePublication(publicationId, studentEmail1);
//        publiService.likePublication(publicationId, studentEmail2);
//
//        List<Likes> likesPubs = publiService.getLikesFromPublication(publicationId);
//
//        assertEquals("Likes for " + publicationId + "don't match ", 2,
//                likesPubs.size());
//    }
//
//
//    @Test
//    public void test_ranking() {
//        float w_per_like = 7;
//        double w_per_visit = 0.01;
//        double ranking = 0;
//        int n = 1000;
//
//        long publicationId = 100;
//
//        Optional<Publication> pub = publiService.getPublication(publicationId);
//
//        assertEquals("Publication id " + publicationId + "not registered", true,
//                pub.isPresent());
//
//        Publication publication = pub.get();
//
//        //init
//        //Asume que accedió n veces a la publicación (Esto debería ir en el controller)
//        for( int i = 0; i < n ; i++) {
//            long visitstmp = publication.getVisits();
//            visitstmp = visitstmp + 1;
//            publication.setVisits(visitstmp);
//        }
//        publiService.updatePublication(publication);
//        //end
//
//        Optional<Publication> pub2 = publiService.getPublicationWithLikes(publicationId);
//
//        assertEquals("Publication id " + publicationId + "not registered", true,
//                pub2.isPresent());
//
//        publication = pub2.get();
//
//        int cantLikes = publication.getPublicationLike().size();
//        ranking = cantLikes * w_per_like + w_per_visit * publication.getVisits();
//
//        publication.setRanking((float) ranking);
//
//        publiService.updatePublication(publication);
//
//        //Retrieve publication to ensure it was stored in bd
//        pub = publiService.getPublication(publicationId);
//
//        assertEquals("Publication id " + publicationId + "not registered", true,
//                pub.isPresent());
//
//        publication = pub.get();
//
//        assertEquals("Raking for ublication id " + publicationId + "is not correct", 34.0,
//                publication.getRanking());
//    }
//
//    @Test
//    public void getPublicationsFromCurator() {
//        String curatorEmail1 = "curator1@utec.edu.pe";
//
//        Optional<User> curatorOptional1 = authService.getUser(curatorEmail1);
//
//        assertEquals("Curator with " + curatorEmail1 + "not registered", true,
//                curatorOptional1.isPresent());
//
//        List<Publication> publications = publiService.getPublicationsFromCurator(curatorEmail1);
//
//        assertEquals("Quantity of curated publications for " + curatorEmail1 + "not match",
//                1, publications.size());
//    }
//
//    @Test
//    public void printPublicationsfromIdCategory() {
//        List<Publication> publications = publiService.getPublicationsFromCategory(2);
//        //Sorted by ranking
//        for (Publication pub: publications) {
//            System.out.println(pub.getId()+"  "+ pub.getRanking());
//        }
//    }
//
//    @Test
//    public void printPublicationsFromTagName() {
//        String tagName1 = "curso_2020";
//        List<Publication> publications = publiService.getPublicationsfromTagName(tagName1);
//
//        //Sorted by ranking
//        for (Publication pub: publications) {
//            System.out.println(pub.getId()+"  "+ pub.getRanking());
//        }
//    }


    public void test_subcomment(Long commentId) {
        String studentmail = "mit.mosquera@lamerced.edu.pe";
        Optional<User> optionalStudent = authService.getUser(studentmail);
        if (!optionalStudent.isPresent())
            return;

        Comment comment1 = new Comment("subcomentario 1", (float) 0.23, new Publication(),
                optionalStudent.get());
        Comment comment2 = new Comment("subcomentario 2", (float) 0.3, new Publication(),
                optionalStudent.get());
        commentService.addCommentToComment(commentId, comment1);
        commentService.addCommentToComment(commentId, comment2);
    }

    public void test_getsubcomment(Long parentcommentId) {
        List<Comment> comments = commentService.getSubcoments(parentcommentId);

        for(Comment comment: comments) {
            System.out.println(comment.getId()+" "+comment.getText_comment()+ " " + comment.getCreatedAt()
                    +" from comment: " + comment.getParentComment().getText_comment());
        }
    }
}
