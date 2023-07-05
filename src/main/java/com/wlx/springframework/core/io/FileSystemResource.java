package com.wlx.springframework.core.io;

import com.wlx.springframework.beans.BeansException;

import java.io.*;

public class FileSystemResource implements Resource {

    private File file;

    private String path;

    public FileSystemResource(File file) {
        this.file = file;
        this.path = file.getPath();
    }

    public FileSystemResource(String path) {
        this.file = new File(path);
        this.path = path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }
}
