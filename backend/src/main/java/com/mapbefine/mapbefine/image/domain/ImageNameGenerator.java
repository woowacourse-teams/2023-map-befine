package com.mapbefine.mapbefine.image.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ImageNameGenerator {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    private ImageNameGenerator() {
    }

    public static String generate(String originalFileName) {
        String fileName = generateUniqueName();
        String extension = ImageExtension.extract(originalFileName);

        return String.join(".", fileName, extension.toLowerCase());
    }

    private static String generateUniqueName() {
        String dateFormat = DATE_TIME_FORMATTER.format(LocalDateTime.now());

        String uuid = UUID.randomUUID().toString();
        String uniqueName = uuid.split("-")[0];

        return String.join("_", dateFormat, uniqueName);
    }

}
