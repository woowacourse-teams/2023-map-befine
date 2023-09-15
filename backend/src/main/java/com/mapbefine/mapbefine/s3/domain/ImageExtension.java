package com.mapbefine.mapbefine.s3.domain;

import static com.mapbefine.mapbefine.s3.exception.S3ErrorCode.ILLEGAL_IMAGE_FILE_EXTENSION;

import com.mapbefine.mapbefine.s3.exception.S3Exception.S3BadRequestException;
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

    public static ImageExtension fromByImageFileName(String imageFileName) {
        return Arrays.stream(values())
                .filter(imageExtension -> imageFileName.endsWith(imageExtension.getExtension()))
                .findFirst()
                .orElseThrow(() -> new S3BadRequestException(ILLEGAL_IMAGE_FILE_EXTENSION));
    }

    public String getExtension() {
        return extension;
    }

}
