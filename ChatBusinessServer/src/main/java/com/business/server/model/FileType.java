package com.business.server.model;

/**
 * La enumeración FileType representa los tipos de archivo admitidos en el sistema de chat.
 * Cada tipo de archivo tiene una extensión asociada.
 */
public enum FileType {
    PDF("pdf"),
    JPG("jpg"),
    JPEG("jpeg");

    private String extension;

    /**
     * Construye un nuevo FileType con la extensión especificada.
     *
     * @param extension la extensión del tipo de archivo
     */
    FileType(String extension) {
        this.extension = extension;
    }

    /**
     * Obtiene la extensión del tipo de archivo.
     *
     * @return la extensión del tipo de archivo
     */
    public String getExtension() {
        return extension;
    }
}
