package com.mapbefine.mapbefine;

import com.mapbefine.mapbefine.s3.application.S3Service;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("test")
public class TestS3ServiceImpl implements S3Service {

    @Override
    public String upload(MultipartFile multipartFile) {
        return "https://map-befine-official.github.io/favicon.png";
    }

}
