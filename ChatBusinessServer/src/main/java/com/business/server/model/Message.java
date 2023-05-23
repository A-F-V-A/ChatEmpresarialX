package com.business.server.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La clase Message representa un mensaje enviado en el sistema de chat.
 * Contiene información sobre el remitente, el contenido del mensaje y la fecha de envío.
 */
public class Message {
    private String nickname;
    private String content;
    private final Date date;

    /**
     * Construye un nuevo objeto Message con el remitente, el contenido y la fecha especificados.
     *
     * @param nickname el nombre del remitente
     * @param content  el contenido del mensaje
     * @param date     la fecha de envío del mensaje
     */
    public Message(String nickname, String content, Date date) {
        this.nickname = nickname;
        this.content = content;
        this.date = date;
    }

    /**
     * Construye un nuevo objeto Message con el remitente, el contenido y la fecha actual.
     *
     * @param nickname el nombre del remitente
     * @param content  el contenido del mensaje
     */
    public Message(String nickname, String content) {
        this(nickname, content, new Date());
    }

    /**
     * Obtiene el nombre del remitente del mensaje.
     *
     * @return el nombre del remitente
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Establece el nombre del remitente del mensaje.
     *
     * @param nickname el nombre del remitente
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Obtiene el contenido del mensaje.
     *
     * @return el contenido del mensaje
     */
    public String getContent() {
        return content;
    }

    /**
     * Establece el contenido del mensaje.
     *
     * @param content el contenido del mensaje
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Obtiene la fecha de envío del mensaje en formato "dd/MM/yyyy HH:mm".
     *
     * @return la fecha de envío del mensaje
     */
    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formatter.format(date);
    }

    /**
     * Devuelve una representación en cadena del mensaje en el formato "code|nickname|content|date".
     *
     * @return la representación en cadena del mensaje
     */
    @Override
    public String toString() {
        return  CommunicationCode.SEND_MESSAGE.getCode() + '|' +
                nickname + '|' +
                content + '|' +
                getDate();
    }
}
