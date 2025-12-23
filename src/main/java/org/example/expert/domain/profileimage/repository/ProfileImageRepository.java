package org.example.expert.domain.profileimage.repository;

import org.example.expert.domain.profileimage.entity.ProfileImage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    @Query("select pi from ProfileImage pi where pi.userId = :userId and pi.filename = :filename")
    Optional<ProfileImage> findByUserIdAndFilename(@Param("userId") Long userId, @Param("filename") String filename);

    Optional<ProfileImage> findFirstByUserId(Long userId, Sort sort);

    Boolean existsByUserIdAndFilename(@Param("userId") Long userId, @Param("filename") String filename);

    List<ProfileImage> findAllByUserId(Long userId);
}
