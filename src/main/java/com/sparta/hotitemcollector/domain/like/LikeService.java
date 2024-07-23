package com.sparta.hotitemcollector.domain.like;

import com.sparta.hotitemcollector.domain.product.Product;
import com.sparta.hotitemcollector.domain.product.ProductService;
import com.sparta.hotitemcollector.domain.product.ProductSimpleResponseDto;
import com.sparta.hotitemcollector.domain.user.User;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final ProductService productService;

    // 좋아요 토글 로직
    public boolean createOrDeleteLike(Long productId, User user) {
        // 좋아요 존재 여부 확인
        Optional<Likes> existingLike = likeRepository.findByProductIdAndUser(productId, user);

        if (existingLike.isPresent()) {
            // 좋아요가 이미 존재하면 삭제
            likeRepository.delete(existingLike.get());
            productService.decreaseLikes(productId);
            return false; // 좋아요 취소
        } else {
            // 좋아요가 존재하지 않으면 생성
            Product product = productService.findById(productId);

            Likes like = Likes.builder()
                    .user(user)
                    .product(product)
                    .build();

            likeRepository.save(like);
            productService.increaseLikes(productId);
            return true; // 좋아요 생성
        }
    }

    @Transactional(readOnly = true)
    public List<ProductSimpleResponseDto> getLikeProduct(User user) {
        List<Product> productList = findLikeProductIdByUser(user);

        return productList.stream()
            .map(ProductSimpleResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Product> findLikeProductIdByUser(User user) {
        List<Likes> likes = likeRepository.findByUser(user);

        return likes.stream()
                .map(Likes::getProduct)
                .toList();
    }
}