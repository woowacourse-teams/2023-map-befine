package com.mapbefine.mapbefine.image.domain;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    Image upload(MultipartFile multipartFile);

    void delete(String imageName);

}
