package com.sparta.hotitemcollector.domain.product.dto;

import com.sparta.hotitemcollector.domain.product.entity.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

@Getter
public class ProductRequestDto {

    @NotBlank(message = "상품의 이름을 입력해주세요.")
    private String name;
    @NotNull(message = "상품의 카테고리를 입력해주세요.")
    private ProductCategory category;
    //@NotBlank(message = "상품의 이미지를 입력해주세요.")
    private List<ProductImageRequestDto> images;
    @NotNull(message = "상품의 가격을 입력해주세요.")
    private Long price;
    @NotBlank(message = "상품의 소개를 입력해주세요.")
    private String info;

    public void addImages(List<ProductImageRequestDto> images) {
        this.images=images;
    }
}
