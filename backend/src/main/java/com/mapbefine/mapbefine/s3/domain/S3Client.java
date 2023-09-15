package com.mapbefine.mapbefine.s3.domain;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3Client {

    @Value("${s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public S3Client(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void upload(MultipartFile multipartFile) throws IOException {
        File tempFile = null;

        try {
            tempFile = File.createTempFile("upload_", ".tmp");
            multipartFile.transferTo(tempFile);
            amazonS3.putObject(new PutObjectRequest(
                    bucket,
                    multipartFile.getOriginalFilename(),
                    tempFile
            ));
        } catch (IOException exception) {
            throw new IOException(exception);
        } finally {
            removeTempFileIfExists(tempFile);
        }
    }

    private void removeTempFileIfExists(File tempFile) {
        if (!Objects.isNull(tempFile) && tempFile.exists()) {
            tempFile.delete();
        }
    }

    public void delete(String key) {
        // 현재는 일단 기능만 만들어놓고, API 는 만들어놓지 않았습니다 회의를 통해서 결정해야 할 사항이 있는 것 같아서요!
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
    }

}
