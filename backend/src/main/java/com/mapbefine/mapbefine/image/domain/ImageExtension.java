package com.mapbefine.mapbefine.image.domain;

import static com.mapbefine.mapbefine.image.exception.ImageErrorCode.ILLEGAL_IMAGE_FILE_EXTENSION;

import com.mapbefine.mapbefine.image.exception.ImageException.ImageBadRequestException;
import java.util.Arrays;

public enum ImageExtension {

    JPEG(".jpeg"),
    JPG(".jpg"),
    JFIF(".jfif"),
    PNG(".png"),
    SVG(".svg"),
    ;

    private final String extension;

    ImageExtension(final String extension) {
        this.extension = extension;
    }

    public static ImageExtension from(String imageFileName) {
        return Arrays.stream(values())
                .filter(imageExtension -> imageFileName.endsWith(imageExtension.getExtension()))
                .findFirst()
                .orElseThrow(() -> new ImageBadRequestException(ILLEGAL_IMAGE_FILE_EXTENSION));
    }

    public String getExtension() {
        return extension;
    }

}
