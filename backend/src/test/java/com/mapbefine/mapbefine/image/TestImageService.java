package com.mapbefine.mapbefine.image;

import com.mapbefine.mapbefine.image.application.ImageService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("test")
public class TestImageService implements ImageService {

    @Override
    public String upload(MultipartFile multipartFile) {
        return "https://map-befine-official.github.io/favicon.png";
    }

}
