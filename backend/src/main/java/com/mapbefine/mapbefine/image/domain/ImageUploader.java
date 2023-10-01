package com.mapbefine.mapbefine.image.domain;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    void upload(MultipartFile multipartFile) throws IOException;

    void delete(String key);

}
