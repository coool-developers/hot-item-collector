package com.sparta.hotitemcollector.domain.product.repository;

import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.domain.product.entity.ProductImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
    List<ProductImage> findAllByProduct(Product product);
    long countByProduct(Product product);

    ProductImage findByIdAndProduct(Long id, Product product);

}
