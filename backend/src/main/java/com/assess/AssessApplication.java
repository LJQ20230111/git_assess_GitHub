package com.assess;

import com.assess.config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssessApplication {

    public static void main(String[] args) {
        EnvLoader.load();
        SpringApplication.run(AssessApplication.class, args);
    }
}
