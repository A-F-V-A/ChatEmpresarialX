package com.business.client.model;


import static com.business.client.util.ValidateFile.getFileExtension;

public enum FileType {
    PDF("pdf"),
    JPG("jpg"),
    JPEG("jpeg");

    private String extension;

    FileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

}
