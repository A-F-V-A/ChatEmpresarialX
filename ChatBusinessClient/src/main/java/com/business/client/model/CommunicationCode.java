package com.business.client.model;

public enum CommunicationCode {
    ESTABLISH_CONNECTION("01"),
    REQUEST_USER_LIST("04"),
    SEND_MESSAGE("05"),
    SEND_FILES("06"),
    CLOSE_SESSION("07");

    private final String code;

    CommunicationCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
