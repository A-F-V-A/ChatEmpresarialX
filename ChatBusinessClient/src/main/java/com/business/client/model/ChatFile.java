package com.business.client.model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.business.client.util.ValidateFile.*;

public class ChatFile {

    private String nickname;
    private final Date date;
    private FileType fileType;
    private String readFileContents;

    public ChatFile(String nickname, Date date,String readFileContents,String fileType){
        this.nickname = nickname;
        this.date = date;
        this.readFileContents = readFileContents;
        try {
            this.fileType = getFileTypeFromFileName("name." + fileType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ChatFile(String nickname, Date date, File file) throws Exception {
        this.nickname = nickname;
        this.date = date;
        validateFile(file);
        this.fileType = getFileTypeFromFileName(file.getName());
        readFileContents = fileToBase64(file);
    }

    public ChatFile (String nickname, File file) throws Exception {
        this(nickname,new Date(),file);
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