package edu.miu.cs.cs489.resumerefinerai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "file:.env", ignoreResourceNotFound = true)
public class ResumeRefinerAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumeRefinerAiApplication.class, args);
    }

}
