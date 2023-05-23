package com.business.client.model;

/**
 * Enumeración que representa los tipos de archivo utilizados en un sistema de chat.
 */
public enum FileType {
    PDF("pdf"),
    JPG("jpg"),
    JPEG("jpeg");

    private final String extension;

    /**
     * Constructor de la enumeración FileType.
     *
     * @param extension Extensión del tipo de archivo.
     */
    FileType(String extension) {
        this.extension = extension;
    }

    /**
     * Obtiene la extensión del tipo de archivo.
     *
     * @return La extensión del tipo de archivo.
     */
    public String getExtension() {
        return extension;
    }

}
