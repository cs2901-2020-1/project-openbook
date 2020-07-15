package com.software.openbook;

import com.software.model.Category;
import com.software.model.Curator;
import com.software.service.AuthService;
import com.software.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan({"com.software"})
@EntityScan("com.software.model")
@EnableJpaRepositories("com.software.repository")
@EnableJpaAuditing
public class OpenbookApplication implements CommandLineRunner {

    @Autowired
    private AuthService authService;

    @Autowired
    private CategoryService categoryService;

    private static final Logger log = LoggerFactory.getLogger(OpenbookApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(OpenbookApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Starting OpenBook ...");
        InitializeCurators();
        InitializeCategories();
    }

    public void InitializeCurators() {
        // create a curador profile
        Curator curator = new Curator("curator@gmail.com","curator123","123456", "Ernesto","Cuadros");
        authService.addUser(curator);

        Curator curator2 = new Curator("curator2@gmail.com","curator2","123456", "Jose","Fiestas");
        authService.addUser(curator2);

    }

    public void InitializeCategories() {

        categoryService.addCategory(new Category(1,"Desarrollo Personal, Ciudadanía y Cívica"));
        categoryService.addCategory(new Category(2,"Ciencias Sociales"));
        categoryService.addCategory(new Category(3, "Educación para el Trabajo"));
        categoryService.addCategory(new Category(4, "Educación Física"));
        categoryService.addCategory(new Category(5, "Comunicación"));
        categoryService.addCategory(new Category(6, "Arte y Cultura"));
        categoryService.addCategory(new Category(7, "Castellano como Segunda Lengua"));
        categoryService.addCategory(new Category(8, "Inglés como Lengua Extranjera"));
        categoryService.addCategory(new Category(9,"Matemática"));
        categoryService.addCategory(new Category(10, "Ciencia y Tecnología"));
        categoryService.addCategory(new Category(11, "Educación Religiosa"));
    }
}