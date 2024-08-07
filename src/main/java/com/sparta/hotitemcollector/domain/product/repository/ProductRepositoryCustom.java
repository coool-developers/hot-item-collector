package com.sparta.hotitemcollector.domain.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sparta.hotitemcollector.domain.product.dto.FilterQueryDto;
import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.domain.product.entity.ProductCategory;
import com.sparta.hotitemcollector.domain.product.entity.ProductStatus;
import com.sparta.hotitemcollector.domain.user.User;

public interface ProductRepositoryCustom {

	public Page<Product> findByRequirement(List<User> users, User user, String productName, ProductCategory category, ProductStatus status, Pageable pageable);

	//Page<Product> findByUserIn(List<User> users, Pageable pageable);

	Page<Product> findTop10ByOrderByLikesDesc(Pageable pageable);

	//Page<Product> findByUserAndStatus(User user, ProductStatus status, Pageable pageable);

	//Page<Product> findByNameContainingIgnoreCase(String productName, Pageable pageable);

	//Page<Product> findByCategory(ProductCategory category, Pageable pageable);

	@Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.id = :id")
	Product findByIdWithImages(@Param("id") Long id);

	List<Product> findByUserAndStatus(User user, ProductStatus status);

	//Page<Product> findByUser(User user, Pageable pageable);

}
