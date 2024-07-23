package com.sparta.hotitemcollector.domain.product;

import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserService userService;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductSimpleResponseDto> getSearchProduct(String nickname, String productName,
        ProductCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> productPage = Page.empty(pageable);

        /*if (nickname != null && !nickname.isEmpty()) {
            List<User> userList = userService.findByNicknameContainingIgnoreCase(nickname);
            if (!userList.isEmpty()) {
                productPage = productRepository.findByUserIn(userList, pageable);
            }
        }*/

        if (productName != null && !productName.isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCase(productName, pageable);
        }

        if (category != null) {
            productPage = productRepository.findByCategory(category, pageable);
        }

        return productPage.getContent()
            .stream()
            .map(ProductSimpleResponseDto::new)
            .collect(Collectors.toList());
    }
}
