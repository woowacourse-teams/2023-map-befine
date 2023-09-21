package com.mapbefine.mapbefine.image.domain;

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
        String extension = extractExtension(originalFileName);

        return new ImageName(fileName + extension);
    }

    private static String extractExtension(String originalFileName) {
        return ImageExtension.from(originalFileName)
                .getExtension();
    }

    public String getFileName() {
        return fileName;
    }

}
