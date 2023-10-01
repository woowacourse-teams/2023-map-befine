package com.mapbefine.mapbefine.image.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mapbefine.mapbefine.image.domain.ImageUploader;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3Uploader implements ImageUploader {

    @Value("${s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public S3Uploader(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
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
        if (Objects.nonNull(tempFile) && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Override
    public void delete(String key) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
    }

}
