package com.sparta.hotitemcollector;

import org.springframework.boot.test.context.TestConfiguration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@TestConfiguration
public class TestConfig {

	@PersistenceContext
	private EntityManager entityManager;
	//
	// @Bean
	// public JPAQueryFactory jpaQueryFactory() {
	// 	return new JPAQueryFactory(entityManager);
	// }
}
