package com.sparta.hotitemcollector.domain.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProductRequestDto {

    @NotBlank(message = "상품의 이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "상품의 카테고리를 입력해주세요.")
    private ProductCategory category;
    @NotBlank(message = "상품의 이미지를 입력해주세요.")
    private String image;
    @NotBlank(message = "상품의 가격을 입력해주세요.")
    private Long price;
    @NotBlank(message = "상품의 소개를 입력해주세요.")
    private String info;

}
