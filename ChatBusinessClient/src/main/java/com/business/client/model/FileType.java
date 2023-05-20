package com.business.client.model;

public enum FileType {
    PDF("pdf", 600),
    JPG("jpg", 600);

    private String extension;
    private int maxSizeKB;

    FileType(String extension, int maxSizeKB) {
        this.extension = extension;
        this.maxSizeKB = maxSizeKB;
    }

    public String getExtension() {
        return extension;
    }

    public int getMaxSizeKB() {
        return maxSizeKB;
    }
}
