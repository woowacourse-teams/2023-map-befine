package com.mapbefine.mapbefine.image.application;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {

    String upload(MultipartFile multipartFile);

}
