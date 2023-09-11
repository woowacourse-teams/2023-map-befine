package com.mapbefine.mapbefine.s3.application;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface S3Service {

    String upload(MultipartFile multipartFile);

}
