package com.mapbefine.mapbefine.image.domain;

import static com.mapbefine.mapbefine.image.exception.ImageErrorCode.ILLEGAL_IMAGE_FILE_EXTENSION;

import com.mapbefine.mapbefine.image.exception.ImageException.ImageBadRequestException;

public enum ImageExtension {

    JPEG, JPG, JFIF, PNG, SVG,
    ;

    public static String extract(String fileName) {
        int index = fileName.lastIndexOf(".") + 1;
        String extension = fileName.substring(index);

        try {
            ImageExtension imageExtension = valueOf(extension.toUpperCase());

            return imageExtension.name().toLowerCase();
        } catch (IllegalArgumentException e) {
            throw new ImageBadRequestException(ILLEGAL_IMAGE_FILE_EXTENSION);
        }
    }

}
