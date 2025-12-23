package org.example.expert.domain.profileimage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.expert.domain.profileimage.entity.ProfileImage;

@Getter
@AllArgsConstructor
public class ProfileImageGetResponse {
    private Long id;
    private Long userId;
    private String filename;

    public static ProfileImageGetResponse from(ProfileImage profileImage) {
        return new ProfileImageGetResponse(
                profileImage.getId(),
                profileImage.getUserId(),
                profileImage.getFilename()
        );
    }
}
