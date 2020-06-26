package com.software.openbook;

import com.software.model.*;
import com.software.service.AuthService;
import com.software.service.CategoryService;
//import com.software.service.PublicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@ComponentScan({"com.software"})
@EntityScan("com.software.model")
@EnableJpaRepositories("com.software.repository")
public class OpenbookApplication implements CommandLineRunner {

    @Autowired
    private AuthService authService;
    @Autowired
    private CategoryService catService;
    @Autowired
    //  private PublicationService publiService;

    private static final Logger log = LoggerFactory.getLogger(OpenbookApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OpenbookApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
/*
        User user = new User("daniel@utec.edu.pe","daniel123","123456", "Daniel", "Rojas");
        Student student = new Student("mit.mosquera@lamerced.edu.pe", "mmosq", "12345", "Mitchael",
                "Mosquera", "La Merced", 7);
        authService.addUser(user);
        authService.addUser(student);
        Professor profe = new Professor("yamilet@utec.edu.pe", "yami", "654321", "Yamilet",
                "Serrano", "Phd. Matematicas");
        authService.addUser(profe);
        //raul_test();
        log.info("Starting OpenBook ...");
    }
    public void raul_test() {
        //Adding categories
        Category cat1 = new Category("Algebra");
        Category cat2 = new Category("Polinomios");
        Category cat3 = new Category("Conteo y Permutaciones");
        catService.addCategory(cat1);
        catService.addCategory(cat2);
        catService.addCategory(cat3);
        Iterable<Category> categories;
        categories = catService.getCategories();
        for(Category element:categories) {
            System.out.println(element.getId()+" "+element.getDescription());
        }
        //adding publication
        String emailProfessor = "yamilet@utec.edu.pe";
        Optional<User> optionalUser = authService.getUser(emailProfessor);
        if (optionalUser.isPresent()) {
            User professor = optionalUser.get();
            Optional<Category> optionalCategory = catService.getCategory(2);
            if (!optionalCategory.isPresent())
                return;
            Publication post1 = new Publication("PDF", "Manual 1234", 0,
                    "\\server\\folder\\test.pdf", (Professor) professor, optionalCategory.get());
            optionalCategory = catService.getCategory(3);
            if (!optionalCategory.isPresent())
                return;
            Publication post2 = new Publication("PDF", "Manual test 12345", 0,
                    "\\server\\folder\\test.pdf", (Professor) professor, optionalCategory.get());
            publiService.addPublication(post1);
            publiService.addPublication(post2);
            List<Publication> publications = publiService.getPublicationFromProfessor(emailProfessor);
            System.out.println("Publications from " + emailProfessor);
            for(Publication publication:publications) {
                System.out.println(publication.getId()+"  "+publication.getName()+" "+publication.getCategory().getDescription());
            }
        } else {
            System.out.println("No professor");
        }*/
    }
}