package com.cocoball.kakaoapipractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KakaoapipracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(KakaoapipracticeApplication.class, args);
    }

}
