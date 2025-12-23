package org.example.expert.domain.profileimage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.expert.domain.profileimage.entity.ProfileImage;

@Getter
@AllArgsConstructor
public class ProfileImageDto {

    private Long id;
    private Long userId;
    private String filename;

    public static ProfileImageDto from(ProfileImage profileImage) {
        return new ProfileImageDto(
                profileImage.getId(),
                profileImage.getUserId(),
                profileImage.getFilename()
        );
    }
}
