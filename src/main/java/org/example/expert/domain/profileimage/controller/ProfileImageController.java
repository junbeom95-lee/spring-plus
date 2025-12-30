package org.example.expert.domain.profileimage.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.profileimage.dto.request.ProfileImageRequest;
import org.example.expert.domain.profileimage.dto.response.ProfileImageCreateResponse;
import org.example.expert.domain.profileimage.dto.response.ProfileImageGetResponse;
import org.example.expert.domain.profileimage.service.ProfileImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profileimages")
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    //프로필 이미지 리스트 얻기
    @GetMapping()
    public ResponseEntity<List<ProfileImageGetResponse>> getList(@AuthenticationPrincipal AuthUser authUser) {

        return ResponseEntity.ok(profileImageService.getList(authUser.getId()));
    }


    //PresignedUrl을 사용해 프로필 이미지를 저장한 후에 호출하는 메서드
    @PutMapping()
    public ResponseEntity<ProfileImageCreateResponse> create(@AuthenticationPrincipal AuthUser authUser,
                                                             @RequestBody ProfileImageRequest request) {

        return ResponseEntity.ok(profileImageService.create(authUser.getId(), request.getFilename()));
    }
}
