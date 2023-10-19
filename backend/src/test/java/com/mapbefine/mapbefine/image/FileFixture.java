package com.mapbefine.mapbefine.image;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class FileFixture {

    public static MultipartFile createFile() {
        return new MockMultipartFile(
                "test",
                "test.png",
                "img/png",
                "image".getBytes()
        );
    }

}
