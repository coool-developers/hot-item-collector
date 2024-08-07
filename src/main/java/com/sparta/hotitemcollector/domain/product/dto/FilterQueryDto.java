package com.sparta.hotitemcollector.domain.product.dto;

import java.util.List;

import com.sparta.hotitemcollector.domain.product.entity.ProductCategory;
import com.sparta.hotitemcollector.domain.product.entity.ProductStatus;
import com.sparta.hotitemcollector.domain.user.User;

import lombok.Getter;

@Getter
public class FilterQueryDto {
	private List<User> users;
	private User user;
	private String productName;
	private ProductCategory productCategory;
	private ProductStatus productStatus;

	public FilterQueryDto(List<User> users) {
		this.users = users;
	}

	public FilterQueryDto(User user, ProductStatus productStatus) {
		this.user = user;
		this.productStatus = productStatus;
	}

	public FilterQueryDto(User user) {
		this.user = user;
	}

	public FilterQueryDto(User user, ProductCategory productCategory) {
		this.user = user;
	}

	public FilterQueryDto(String productName){
		this.productName = productName;
	}

	public FilterQueryDto(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}
}
