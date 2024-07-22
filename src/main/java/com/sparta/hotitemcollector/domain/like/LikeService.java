package com.sparta.hotitemcollector.domain.like;

import com.sparta.hotitemcollector.domain.product.Product;
import com.sparta.hotitemcollector.domain.product.ProductRepository;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final ProductRepository productRepository;

    // 좋아요 토글 로직
    public boolean createOrDeleteLike(Long productId, User user) {
        // 좋아요 존재 여부 확인
        Optional<Likes> existingLike = likeRepository.findByProductIdAndUser(productId, user);

        if (existingLike.isPresent()) {
            // 좋아요가 이미 존재하면 삭제
            likeRepository.delete(existingLike.get());
            return false; // 좋아요 취소
        } else {
            // 좋아요가 존재하지 않으면 생성
            // ToDo : ProductService에서 체크 메서드 생성 시 Service레이어 참조하도록 변경
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

            Likes like = Likes.builder()
                    .user(user)
                    .product(product)
                    .build();

            likeRepository.save(like);
            return true; // 좋아요 생성
        }
    }
}
