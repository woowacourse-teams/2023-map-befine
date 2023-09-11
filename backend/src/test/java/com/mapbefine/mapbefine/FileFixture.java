package com.mapbefine.mapbefine;

import org.springframework.web.multipart.MultipartFile;

public class FileFixture {

    public static MultipartFile createFile() {
        return new StubFile();
    }

}
