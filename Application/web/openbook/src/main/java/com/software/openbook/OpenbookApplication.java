package com.software.openbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(OpenbookApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(OpenbookApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Starting OpenBook ...");
    }
}