package com.business.server.managers;

import com.business.server.model.ChatFile;
import com.business.server.model.FileType;
import com.business.server.model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageFileManager {
    private List<ChatFile> messageList;

    public MessageFileManager() {
        messageList = new ArrayList<>();
    }

    public synchronized void addMessage(String sender, String content, String  timestamp, FileType type) {
        Date date = new Date(timestamp);
        ChatFile message = new ChatFile(sender, date,content,type);
        System.out.println(message.toString());
        messageList.add(message);
    }

    public synchronized List<ChatFile> getMessageList() {
        return new ArrayList<>(messageList);
    }
}
