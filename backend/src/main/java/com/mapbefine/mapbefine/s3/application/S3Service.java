package com.mapbefine.mapbefine.s3.application;

import com.mapbefine.mapbefine.s3.domain.S3Client;
import com.mapbefine.mapbefine.s3.domain.UploadFile;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    @Value("prefix-upload-path")
    private String prefixUploadPath;
    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String upload(MultipartFile multipartFile) {
        try {
            UploadFile uploadFile = UploadFile.of(multipartFile);
            s3Client.upload(uploadFile);
            return getUploadPath(uploadFile);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String getUploadPath(final UploadFile uploadFile) {
        return String.join(
                "/",
                prefixUploadPath,
                uploadFile.getOriginalFilename()
        );
    }

}

