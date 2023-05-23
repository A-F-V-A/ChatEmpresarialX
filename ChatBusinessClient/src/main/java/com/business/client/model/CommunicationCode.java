package com.business.client.model;

/**
 * Enumeración que representa los códigos de comunicación utilizados en un sistema de chat.
 */
public enum CommunicationCode {
    ESTABLISH_CONNECTION("01"),
    REQUEST_USER_LIST("04"),
    SEND_MESSAGE("05"),
    SEND_FILES("06"),
    CLOSE_SESSION("07");

    private final String code;

    /**
     * Constructor de la enumeración CommunicationCode.
     *
     * @param code Código de comunicación asociado.
     */
    CommunicationCode(String code) {
        this.code = code;
    }

    /**
     * Obtiene el código de comunicación.
     *
     * @return El código de comunicación asociado.
     */
    public String getCode() {
        return code;
    }
}
