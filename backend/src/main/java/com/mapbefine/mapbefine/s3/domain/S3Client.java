package com.mapbefine.mapbefine.s3.domain;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
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

    public void upload(MultipartFile multipartFile) {
        File tempFile = null;

        try {
            tempFile = File.createTempFile("upload_", ".tmp");
            multipartFile.transferTo(tempFile);
            amazonS3.putObject(new PutObjectRequest(bucket, multipartFile.getOriginalFilename(), tempFile));
        } catch (IOException e) { // TODO: 2023/09/07 Exception 을 수정
            throw new RuntimeException(e);
        } finally {
            removeTempFileIfExists(tempFile);
        }
    }

    private void removeTempFileIfExists(final File tempFile) {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    public void delete(String key) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
    }

}
