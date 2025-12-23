package org.example.expert.domain.profileimage.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.profileimage.dto.ProfileImageDto;
import org.example.expert.domain.profileimage.dto.response.ProfileImageGetResponse;
import org.example.expert.domain.profileimage.enums.ImageExtension;
import org.example.expert.domain.profileimage.repository.ProfileImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileImageS3Service {

    @Value("${cloud.aws.bucket-name}")
    private String bucket;

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private final ProfileImageRepository profileImageRepository;
    private final ProfileImageService profileImageService;

    //프로필 이미지 링크 얻기
    public String createPresignedGetUrl(Long userId) {

        ProfileImageDto profileImage = profileImageService.getRecentProfileImage(userId);

        String key = getKey(userId, profileImage.getFilename());

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(objectRequest)
                .build();

        return s3Presigner
                .presignGetObject(presignRequest)
                .url()
                .toExternalForm();
    }

    //프로필 이미지 링크 리스트 얻기
    public List<String> createPresignedGetListUrl(Long userId) {

        List<ProfileImageGetResponse> list = profileImageService.getList(userId);

        return list.stream()
                .map(pi -> {
                    String key = getKey(userId, pi.getFilename());

                    GetObjectRequest objectRequest = GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build();

                    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                            .signatureDuration(Duration.ofMinutes(10))
                            .getObjectRequest(objectRequest)
                            .build();

                    return s3Presigner.presignGetObject(presignRequest)
                            .url().toExternalForm();
                })
                .toList();
    }

    //프로필 이미지 업로드할 링크 얻기
    public String createPresignedUrl(Long userId, String filename) {

        boolean existsByIdAndFilename = profileImageRepository.existsByUserIdAndFilename(userId, filename);

        if (existsByIdAndFilename) throw new InvalidRequestException("이미 존재하는 파일명입니다.");

        String fileExtension = filename.substring(filename.lastIndexOf('.') + 1);

        String contentType = ImageExtension.from(fileExtension);

        String key = getKey(userId, filename);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .putObjectRequest(putObjectRequest)
                        .build();

        return s3Presigner
                .presignPutObject(presignRequest)
                .url()
                .toExternalForm();
    }

    //프로필 S3 이미지 삭제
    public void deleteObject(Long userId, String filename) {

        String key = getKey(userId, filename);

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);

        profileImageService.deleteObject(userId, filename);
    }

    //userId와 파일이름으로 key만들기
    private String getKey(Long userId, String filename) {

        return "profileimage/userid:" + userId + "/" + filename;
    }


}
