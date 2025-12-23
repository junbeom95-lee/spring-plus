package org.example.expert.domain.profileimage.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.profileimage.dto.response.ProfileImageCreateResponse;
import org.example.expert.domain.profileimage.dto.ProfileImageDto;
import org.example.expert.domain.profileimage.dto.response.ProfileImageGetResponse;
import org.example.expert.domain.profileimage.entity.ProfileImage;
import org.example.expert.domain.profileimage.repository.ProfileImageRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;

    @Transactional(readOnly = true)
    public ProfileImageDto getRecentProfileImage(Long userId) {

        Sort sort = Sort.by("id").descending();

        ProfileImage profileImage = profileImageRepository.findFirstByUserId(userId, sort).orElseThrow(
                () -> new InvalidRequestException("ProfileImage not found"));

        return ProfileImageDto.from(profileImage);
    }

    //이미지 리스트 확인
    @Transactional(readOnly = true)
    public List<ProfileImageGetResponse> getList(Long userId) {

        List<ProfileImage> profileImageList = profileImageRepository.findAllByUserId(userId);

        return profileImageList.stream()
                .map(ProfileImageGetResponse::from)
                .toList();
    }

    //이미지 업로드
    @Transactional
    public ProfileImageCreateResponse create(Long userId, String filename) {

        ProfileImage profileImage = new ProfileImage(userId, filename);

        ProfileImage savedProfileImage = profileImageRepository.save(profileImage);

        return ProfileImageCreateResponse.from(savedProfileImage);
    }

    //이미지 삭제
    @Transactional
    public void deleteObject(Long userId, String filename) {

        ProfileImage profileImage = profileImageRepository.findByUserIdAndFilename(userId, filename)
                .orElseThrow(() -> new InvalidRequestException("ProfileImage not found"));

        profileImageRepository.delete(profileImage);

    }
}
