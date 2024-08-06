package com.sparta.hotitemcollector.domain.like;

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
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class LikeRepositoryTest {

	@Autowired
	LikeRepository likeRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("1. findByProductIdAndUser를 테스트한다")
	void Test1() {
		//given
		Long productId = 1L;
		User user = userRepository.findById(1L).orElse(null);

		// when
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 시작 >>>>>>>>>>>>>>>>>>>>>>>>>");
		long before = System.currentTimeMillis();
		Likes likes = likeRepository.findByProductIdAndUser(productId, user).orElse(null);
		long after = System.currentTimeMillis();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 종료 >>>>>>>>>>>>>>>>>>>>>>>>>");

		// then
		System.out.println("총 걸린 시간 : " + (after - before) + "ms");
		System.out.println("====================================");
	}

	@Test
	@DisplayName("2. findByUser를 테스트한다")
	void test2() {
		//given
		User user = userRepository.findById(1L).orElse(null);
		Pageable pageable = PageRequest.of(1, 16, Sort.by(Sort.Direction.DESC, "createdAt"));

		// when
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 시작 >>>>>>>>>>>>>>>>>>>>>>>>>");
		long before = System.currentTimeMillis();
		Page<Likes> likePage = likeRepository.findByUser(user, pageable);
		long after = System.currentTimeMillis();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 종료 >>>>>>>>>>>>>>>>>>>>>>>>>");

		// then
		System.out.println("총 걸린 시간 : " + (after - before) + "ms");
		System.out.println("====================================");
	}
}