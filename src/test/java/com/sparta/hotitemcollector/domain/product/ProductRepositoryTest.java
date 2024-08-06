package com.sparta.hotitemcollector.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.sparta.hotitemcollector.TestConfig;
import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.domain.product.repository.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class ProductRepositoryTest {

	@Autowired
	ProductRepository productRepository;

	@Test
	@DisplayName("1. findByNameContaining를 테스트한다")
	void test1() {
		//given
		Pageable pageable = PageRequest.of(1, 16, Sort.by(Sort.Direction.DESC, "createdAt"));

		// when
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 시작 >>>>>>>>>>>>>>>>>>>>>>>>>");
		long before = System.currentTimeMillis();
		Page<Product> productPage = productRepository.findByNameContainingIgnoreCase("name", pageable);
		long after = System.currentTimeMillis();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 종료 >>>>>>>>>>>>>>>>>>>>>>>>>");

		// then
		System.out.println("총 걸린 시간 : " + (after - before) + "ms");
		System.out.println("====================================");
	}


}
