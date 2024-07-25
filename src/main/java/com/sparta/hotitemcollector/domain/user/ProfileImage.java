package com.sparta.hotitemcollector.domain.user;

import com.sparta.hotitemcollector.domain.user.dto.user.ProfileImageRequestDto;
import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@Table(name = "profile_image")
@RequiredArgsConstructor
public class ProfileImage extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_name")
    private String filename;
    @Column(name = "image_url")
    private String imageUrl;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ProfileImage(ProfileImageRequestDto profileImage,User user) {
        this.filename=profileImage.getFilename();
        this.imageUrl= profileImage.getImageUrl();
        this.user=user;
    }
}
