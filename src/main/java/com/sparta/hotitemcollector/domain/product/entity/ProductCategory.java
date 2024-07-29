package com.sparta.hotitemcollector.domain.product.entity;

public enum ProductCategory {
    FOOD("식품"),
    BEAUTY("뷰티"),
    FASHION("패션&주얼리"),
    CRAFTS("공예품"),
    HOME_LIVING("홈리빙"),
    PET("반려동물");

    private String category;

    ProductCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }
}
