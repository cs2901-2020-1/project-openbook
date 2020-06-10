package com.software.openbook;

import com.software.model.User;
import com.software.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.software"})
@EntityScan("com.software.model")
@EnableJpaRepositories("com.software.repository")
public class OpenbookApplication implements CommandLineRunner {

    @Autowired
    private AuthService authService;

    private static final Logger log = LoggerFactory.getLogger(OpenbookApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OpenbookApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        User user = new User("daniel@utec.edu.pe","daniel123","123456");

        authService.addUser(user);

        log.info("Starting OpenBook ...");
    }
}
