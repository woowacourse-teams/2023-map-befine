package com.mapbefine.mapbefine.image;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import org.springframework.mock.web.MockMultipartFile;

public class FileFixture {

    public static MockMultipartFile createFile() {
        return new MockMultipartFile(
                "image",
                "image.png",
                IMAGE_PNG_VALUE,
                "data".getBytes()
        );
    }

}
