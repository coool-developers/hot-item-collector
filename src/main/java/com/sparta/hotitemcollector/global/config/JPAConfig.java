package com.sparta.hotitemcollector.global.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JPAConfig {
    @PersistenceContext
    private EntityManager em;

//    @Bean
//    public JPAQueryFactory jpaQueryFactory() {
//        return new JPAQueryFactory(em);
//    }
}
