package com.business.client.model;


import java.util.ArrayList;
import java.util.List;

public class Chat {
    private List<Message> messages;
    private List<File> files;

    public Chat() {
        messages = new ArrayList<>();
        files = new ArrayList<>();
    }

    // Métodos para agregar y obtener mensajes
    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    // Métodos para agregar y obtener archivos
    public void addFile(File file) {
        files.add(file);
    }

    public List<File> getFiles() {
        return files;
    }
}
