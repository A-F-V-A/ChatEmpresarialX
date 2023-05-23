package com.business.server.model;

/**
 * La enumeración CommunicationCode contiene los códigos de comunicación utilizados en el sistema de chat.
 * Cada código de comunicación representa una acción específica en el protocolo de comunicación.
 */
public enum CommunicationCode {
    ESTABLISH_CONNECTION("01"),
    REQUEST_USER_LIST("04"),
    SEND_MESSAGE("05"),
    SEND_FILES("06"),
    CLOSE_SESSION("07");

    private final String code;

    /**
     * Construye un nuevo CommunicationCode con el código especificado.
     *
     * @param code el código de comunicación
     */
    CommunicationCode(String code) {
        this.code = code;
    }

    /**
     * Obtiene el código de comunicación.
     *
     * @return el código de comunicación
     */
    public String getCode() {
        return code;
    }
}
