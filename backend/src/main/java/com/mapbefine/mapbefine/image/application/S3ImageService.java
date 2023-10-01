package com.mapbefine.mapbefine.image.application;

import com.mapbefine.mapbefine.image.domain.ImageUploader;
import com.mapbefine.mapbefine.image.domain.UploadFile;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("!test")
public class S3ImageService implements ImageService {

    @Value("${prefix.upload.path}")
    private String prefixUploadPath;
    private final ImageUploader imageUploader;

    public S3ImageService(ImageUploader imageUploader) {
        this.imageUploader = imageUploader;
    }

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            UploadFile uploadFile = UploadFile.from(multipartFile);
            imageUploader.upload(uploadFile);
            return getUploadPath(uploadFile);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String getUploadPath(UploadFile uploadFile) {
        return String.join(
                "/",
                prefixUploadPath,
                uploadFile.getOriginalFilename()
        );
    }

}

