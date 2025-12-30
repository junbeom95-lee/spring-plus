package org.example.expert.domain.profileimage.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.profileimage.dto.request.ProfileImageRequest;
import org.example.expert.domain.profileimage.service.ProfileImageS3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3/profileimages")
public class ProfileImageS3Controller {

    private final ProfileImageS3Service profileImageS3Service;

    //프로필 이미지 URL 얻기
    @GetMapping("/{userId}")
    public ResponseEntity<String> createPresignedGetUrl(@PathVariable Long userId) {
        return ResponseEntity.ok(profileImageS3Service.createPresignedGetUrl(userId));
    }

    //프로필 이미지 URL 리스트 얻기
    @GetMapping()
    public ResponseEntity<List<String>> createPresignedGetListUrl(@AuthenticationPrincipal AuthUser authUser) {

        return ResponseEntity.ok(profileImageS3Service.createPresignedGetListUrl(authUser.getId()));
    }


    //프로필 이미지 업로드 URL 얻기
    @PutMapping()
    public ResponseEntity<String> createPresignedUrl(@AuthenticationPrincipal AuthUser authUser,
                                                     @RequestBody ProfileImageRequest request) {

        return ResponseEntity.ok(profileImageS3Service.createPresignedUrl(authUser.getId(), request.getFilename()));

    }

    //프로필 이미지 삭제
    @DeleteMapping()
    public ResponseEntity<Void> deleteObject(@AuthenticationPrincipal AuthUser authUser,
                                               @RequestBody ProfileImageRequest request) {

        profileImageS3Service.deleteObject(authUser.getId(), request.getFilename());

        return ResponseEntity.ok().build();
    }
}
