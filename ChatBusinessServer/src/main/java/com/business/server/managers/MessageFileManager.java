package com.business.server.managers;

import com.business.server.model.ChatFile;
import com.business.server.model.FileType;
import com.business.server.model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * La clase MessageFileManager gestiona el almacenamiento y recuperación de mensajes de archivos de chat.
 * Proporciona métodos para agregar nuevos mensajes de archivo de chat y obtener la lista de mensajes.
 */
public class MessageFileManager {
    private List<ChatFile> messageList;

    /**
     * Construye un nuevo objeto MessageFileManager.
     * Inicializa la lista de mensajes de archivos de chat.
     */
    public MessageFileManager() {
        messageList = new ArrayList<>();
    }

    /**
     * Agrega un nuevo mensaje de archivo de chat a la lista de mensajes.
     *
     * @param sender    el remitente del mensaje de archivo de chat
     * @param content   el contenido del archivo de chat
     * @param timestamp el sello de tiempo del mensaje de archivo de chat
     * @param type      el tipo de archivo de chat (por ejemplo, JPG, JPEG, PDF)
     */
    public synchronized void addMessage(String sender, String content, String  timestamp, FileType type) {
        Date date = new Date(timestamp);
        ChatFile message = new ChatFile(sender, date,content,type);
        System.out.println(message.toString());
        messageList.add(message);
    }

    /**
     * Devuelve una copia de la lista de mensajes.
     *
     * @return una lista de mensajes de archivo de chat
     */
    public synchronized List<ChatFile> getMessageList() {
        return new ArrayList<>(messageList);
    }
}
