package com.business.client.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class File {
    private String nickname;
    private String fileName;
    private String filePath;

    private Date date;
    public File(String nickname, String fileName, Date date, String filePath) {
        this.nickname = nickname;
        this.fileName = fileName;
        this.date = date;
        this.filePath = filePath;
    }

    public File(String nickname, String fileName, String filePath) {
        this(nickname, fileName, new Date(), filePath);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formatter.format(date);
    }

}