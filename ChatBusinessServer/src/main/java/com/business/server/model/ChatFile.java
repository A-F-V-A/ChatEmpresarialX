package com.business.server.model;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatFile {

    private String nickname;
    private  Date date;
    private FileType fileType;
    private String readFileContents;

    public ChatFile(String nickname, Date date, String file, FileType type) {
        this.nickname = nickname;
        this.date = date;
        this.fileType = type;
        this.readFileContents = file;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formatter.format(date);
    }

    public String getReadFileContents() {
        return readFileContents;
    }

    public FileType getFileType() {
        return fileType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
                .append(CommunicationCode.SEND_FILES.getCode()).append("|")
                .append(nickname).append("|")
                .append(getDate()).append("|")
                .append(fileType.getExtension()).append("|")
                .append(readFileContents);
        return sb.toString();
    }
}
