package com.sparta.hotitemcollector.domain.like;

import lombok.Getter;

@Getter
public class UserLikeResponseDto {
	private boolean isUserLike;

	public UserLikeResponseDto(boolean isUserLike) {
		this.isUserLike = isUserLike;
	}
}
