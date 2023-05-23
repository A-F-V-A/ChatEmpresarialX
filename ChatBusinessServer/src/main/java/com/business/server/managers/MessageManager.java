package com.business.server.managers;
import com.business.server.model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * La clase MessageManager gestiona el almacenamiento y recuperación de mensajes de chat.
 * Proporciona métodos para agregar nuevos mensajes y obtener la lista de mensajes.
 */
public class MessageManager {

    private List<Message> messageList;

    /**
     * Construye un nuevo objeto MessageManager.
     * Inicializa la lista de mensajes.
     */
    public MessageManager() {
        messageList = new ArrayList<>();
    }

    /**
     * Agrega un nuevo mensaje a la lista de mensajes.
     *
     * @param sender    el remitente del mensaje
     * @param content   el contenido del mensaje
     * @param timestamp el sello de tiempo del mensaje
     */
    public synchronized void addMessage(String sender, String content, String  timestamp) {
        Date date = new Date(timestamp);
        Message message = new Message(sender, content, date);
        System.out.println(message.toString());
        messageList.add(message);
    }

    /**
     * Devuelve una copia de la lista de mensajes.
     *
     * @return una lista de mensajes
     */
    public synchronized List<Message> getMessageList() {
        return new ArrayList<>(messageList);
    }
}