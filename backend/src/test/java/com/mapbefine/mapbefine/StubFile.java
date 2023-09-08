package com.mapbefine.mapbefine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import org.springframework.web.multipart.MultipartFile;

public class StubFile implements MultipartFile {

    private final String fileName;
    private final byte[] bytes;

    public StubFile() {
        this.fileName = "yyyyMMddHHmmssSSSSSS";
        this.bytes = fileName.getBytes();
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
        return "text/plain";
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
    public byte[] getBytes() throws IOException {
        return Base64.getEncoder()
                .encode(bytes);
    }

    @Override
    public InputStream getInputStream() {
        return InputStream.nullInputStream();
    }

    @Override
    public void transferTo(final File dest) throws IOException, IllegalStateException {
        FileOutputStream fileOutputStream = new FileOutputStream(dest);
        fileOutputStream.write(this.bytes);
        fileOutputStream.close();
    }

}
