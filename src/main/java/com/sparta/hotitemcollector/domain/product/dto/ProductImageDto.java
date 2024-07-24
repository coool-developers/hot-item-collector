package com.sparta.hotitemcollector.domain.product.dto;

import com.sparta.hotitemcollector.domain.product.entity.ProductImage;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ProductImageDto {

    @NotBlank(message = "이미지 파일 이름을 입력해주세요.")
    private String filename;
    @NotBlank(message = "이미지 URL을 입력해주세요.")
    private String imageUrl;

    public ProductImageDto(String filename, String imageUrl) {
        this.filename=filename;
        this.imageUrl=imageUrl;
    }

    public ProductImageDto(ProductImage productImage) {
        this.filename= productImage.getFilename();
        this.imageUrl= productImage.getImageUrl();
    }
}
