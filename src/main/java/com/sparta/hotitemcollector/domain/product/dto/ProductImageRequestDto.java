package com.sparta.hotitemcollector.domain.product.dto;

import com.sparta.hotitemcollector.domain.product.entity.ProductImage;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProductImageRequestDto {

    @NotBlank(message = "이미지 파일 이름을 입력해주세요.")
    private String filename;
    @NotBlank(message = "이미지 URL을 입력해주세요.")
    private String imageUrl;

    public ProductImageRequestDto(String filename, String imageUrl) {
        this.filename=filename;
        this.imageUrl=imageUrl;
    }
}
