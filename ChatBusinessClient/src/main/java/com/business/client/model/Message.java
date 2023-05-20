package com.business.client.model;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String nickname;
    private String content;
    private Date date;

    public Message(String nickname, String content, Date date) {
        this.nickname = nickname;
        this.content = content;
        this.date = date;
    }

    public Message(String nickname, String content) {
        this(nickname, content, new Date());
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formatter.format(date);
    }

}