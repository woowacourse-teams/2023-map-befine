package com.mapbefine.mapbefine.image.infrastructure;

import static com.mapbefine.mapbefine.image.exception.ImageErrorCode.UNKNOWN_SERVER_ERROR;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mapbefine.mapbefine.image.domain.Image;
import com.mapbefine.mapbefine.image.domain.ImageNameGenerator;
import com.mapbefine.mapbefine.image.domain.ImageUploader;
import com.mapbefine.mapbefine.image.exception.ImageException.ImageInternalServerException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3ImageUploader implements ImageUploader {

    @Value("${s3.bucket}")
    private String bucket;
    @Value("${s3.folder}")
    private String folder;
    @Value("${prefix.upload.path}")
    private String prefixUploadPath;
    private final AmazonS3 amazonS3;

    public S3ImageUploader(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public Image upload(MultipartFile multipartFile) {
        String imageName = ImageNameGenerator.generate(multipartFile.getContentType());
        ObjectMetadata metadata = getMetadata(multipartFile);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectRequest request = createPutObjectRequest(imageName, inputStream, metadata);
            amazonS3.putObject(request);

            return getImage(imageName);
        } catch (IOException exception) {
            throw new ImageInternalServerException(UNKNOWN_SERVER_ERROR);
        }
    }

    private PutObjectRequest createPutObjectRequest(
            String imageName,
            InputStream inputStream,
            ObjectMetadata metadata
    ) {
        String key = folder + imageName;

        return new PutObjectRequest(bucket, key, inputStream, metadata);
    }

    private ObjectMetadata getMetadata(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        return metadata;
    }

    private Image getImage(String imageName) {
        String imageUrl = String.join(
                "/",
                prefixUploadPath,
                imageName
        );

        return Image.from(imageUrl);
    }

    @Override
    public void delete(String imageName) {
        String key = folder + imageName;
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, key);

        amazonS3.deleteObject(request);
    }

}
