package com.sparta.hotitemcollector.domain.follow;

import lombok.Getter;

@Getter
public class UserFollowResponseDto {
	private boolean isUserFollow;

	public UserFollowResponseDto(boolean isUserFollow) {
		this.isUserFollow = isUserFollow;
	}
}
