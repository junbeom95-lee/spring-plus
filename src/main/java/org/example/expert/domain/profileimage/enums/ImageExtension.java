package org.example.expert.domain.profileimage.enums;

import lombok.Getter;
import org.example.expert.domain.common.exception.InvalidRequestException;

import java.util.Arrays;

@Getter
public enum ImageExtension {
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),

    ;

    private final String extension;
    private final String contentType;

    ImageExtension(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public static String from(String extension) {
        ImageExtension imageExtension = Arrays.stream(values())
                .filter(type -> type.extension.equalsIgnoreCase(extension))
                .findFirst()
                .orElseThrow( () -> new InvalidRequestException("지원하지 않는 확장자입니다."));

        return imageExtension.contentType;
    }
}
