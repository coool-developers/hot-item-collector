package com.sparta.hotitemcollector.domain.follow;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.sparta.hotitemcollector.TestConfig;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class FollowRepositoryTest {

	@Autowired
	FollowRepository followRepository;
	@Autowired
	UserRepository userRepository;

	@Test
	@DisplayName("1. existByFollowerIdAndFollowingId를 테스트한다")
	void test1() {
		//given
		Long followerId = 1L;
		Long followingId = 2L;

		// when
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 시작 >>>>>>>>>>>>>>>>>>>>>>>>>");
		long before = System.currentTimeMillis();
		boolean isFollowing = followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
		long after = System.currentTimeMillis();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 종료 >>>>>>>>>>>>>>>>>>>>>>>>>");

		// then
		System.out.println("총 걸린 시간 : " + (after - before) + "ms");
		System.out.println("====================================");
	}

	@Test
	@DisplayName("2. findByFollowerIdAndFollowingId를 테스트한다")
	void test2() {
		//given
		Long followerId = 1L;
		Long followingId = 2L;

		// when
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 시작 >>>>>>>>>>>>>>>>>>>>>>>>>");
		long before = System.currentTimeMillis();
		Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId).orElse(null);
		long after = System.currentTimeMillis();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 종료 >>>>>>>>>>>>>>>>>>>>>>>>>");

		// then
		System.out.println("총 걸린 시간 : " + (after - before) + "ms");
		System.out.println("====================================");
	}

	@Test
	@DisplayName("3. findByFollowerId를 테스트한다")
	void test3() {
		//given
		Long id = 1L;

		// when
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 시작 >>>>>>>>>>>>>>>>>>>>>>>>>");
		long before = System.currentTimeMillis();
		List<Follow> follow = followRepository.findByFollowerId(id);
		long after = System.currentTimeMillis();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 종료 >>>>>>>>>>>>>>>>>>>>>>>>>");

		// then
		System.out.println("총 걸린 시간 : " + (after - before) + "ms");
		System.out.println("====================================");
	}

	@Test
	@DisplayName("4. countByFollowing를 테스트한다")
	void test4() {
		//given
		User user = userRepository.findById(1L).orElse(null);

		// when
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 시작 >>>>>>>>>>>>>>>>>>>>>>>>>");
		long before = System.currentTimeMillis();
		Long count = followRepository.countByFollowing(user);
		long after = System.currentTimeMillis();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> 메소드 종료 >>>>>>>>>>>>>>>>>>>>>>>>>");

		// then
		System.out.println("총 걸린 시간 : " + (after - before) + "ms");
		System.out.println("====================================");
	}

}
