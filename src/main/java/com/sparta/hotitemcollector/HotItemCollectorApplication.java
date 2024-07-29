package com.sparta.hotitemcollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HotItemCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotItemCollectorApplication.class, args);
    }

}
