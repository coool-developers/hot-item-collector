package com.sparta.hotitemcollector.domain.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.hotitemcollector.domain.product.dto.ProductImageDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일 저장
    public List<ProductImageDto> uploadFiles(List<MultipartFile> files) throws IOException {
        List<ProductImageDto> productImageDtos = new ArrayList<>();

        for (MultipartFile file : files) {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // S3에 파일 업로드
            amazonS3.putObject(bucket, filename, file.getInputStream(), metadata);

            // 업로드된 파일의 URL을 가져오기
            String imageUrl = amazonS3.getUrl(bucket, filename).toString();

            // ProductImageDto 생성
            ProductImageDto productImageDto = new ProductImageDto(filename, imageUrl);
            productImageDtos.add(productImageDto);
        }

        return productImageDtos;
    }


    // 파일 삭제
    public void deleteImage(String originalFilename)  {
        amazonS3.deleteObject(bucket, originalFilename);
    }
}