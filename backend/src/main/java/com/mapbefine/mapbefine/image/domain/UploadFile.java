package com.mapbefine.mapbefine.image.domain;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public class UploadFile implements MultipartFile {

    private final String fileName;
    private final byte[] bytes;

    private UploadFile(
            String fileName,
            byte[] bytes
    ) {
        this.fileName = fileName;
        this.bytes = bytes;
    }

    public static UploadFile from(
            MultipartFile multipartFile
    ) throws IOException {
        ImageName imageName = ImageName.from(multipartFile.getOriginalFilename());
        byte[] multipartFileBytes = multipartFile.getBytes();

        return new UploadFile(imageName.getFileName(), multipartFileBytes);
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getOriginalFilename() {
        return fileName;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public Resource getResource() {
        return MultipartFile.super
                .getResource();
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(dest)) {
            fileOutputStream.write(bytes);
        }
    }

}
