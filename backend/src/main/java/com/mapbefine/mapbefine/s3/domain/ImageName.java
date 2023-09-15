package com.mapbefine.mapbefine.s3.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImageName {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS");

    private final String fileName;

    private ImageName(String fileName) {
        this.fileName = fileName;
    }

    public static ImageName from(String originalFileName) {
        String fileName = FORMATTER.format(LocalDateTime.now());
        String extension = getExtension(originalFileName).getExtension();

        return new ImageName(fileName + extension);
    }

    private static ImageExtension getExtension(String originalFileName) {
        return ImageExtension.fromByImageFileName(originalFileName);
    }

    public String getFileName() {
        return fileName;
    }

}
