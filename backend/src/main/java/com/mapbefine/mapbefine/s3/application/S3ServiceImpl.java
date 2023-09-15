package com.mapbefine.mapbefine.s3.application;

import com.mapbefine.mapbefine.s3.domain.S3Client;
import com.mapbefine.mapbefine.s3.domain.UploadFile;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("!test") // Test 시에는 TestS3ServiceImpl 를 Component 로 띄우기 위해 Profile 을 분리하였습니다.
public class S3ServiceImpl implements S3Service {

    @Value("${prefix.upload.path}")
    private String prefixUploadPath;
    private final S3Client s3Client;

    public S3ServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            UploadFile uploadFile = UploadFile.from(multipartFile);
            s3Client.upload(uploadFile);
            return getUploadPath(uploadFile);
        } catch (IOException exception) { // 이거 어떻게 처리해야할까요..
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

