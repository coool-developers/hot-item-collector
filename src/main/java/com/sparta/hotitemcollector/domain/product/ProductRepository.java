package com.sparta.hotitemcollector.domain.product;

import com.sparta.hotitemcollector.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByUserIn(List<User> users);

    List<Product> findTop10ByOrderByLikesDesc();

    List<Product> findByUserAndStatus(User user, ProductStatus status);
}
