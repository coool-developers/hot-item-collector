package com.sparta.hotitemcollector.domain.product.repository;

import com.sparta.hotitemcollector.domain.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
}
