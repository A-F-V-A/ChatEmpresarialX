package com.business.client.model;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase que representa un mensaje en un chat.
 */
public class Message {
    private final String nickname;
    private final String content;
    private final Date date;


    /**
     * Constructor de la clase Message que recibe el apodo del usuario, el contenido del mensaje y la fecha.
     *
     * @param nickname            Apodo del usuario que envía el mensaje.
     * @param content             Contenido del mensaje.
     * @param date                Fecha del mensaje.
     */
    public Message(String nickname, String content, Date date) {
        this.nickname = nickname;
        this.content = content;
        this.date = date;
    }

    /**
     * Constructor de la clase Message que recibe el apodo del usuario y el contenido del mensaje.
     * La fecha se establece como la fecha actual.
     *
     * @param nickname            Apodo del usuario que envía el mensaje.
     * @param content             Contenido del mensaje.
     */
    public Message(String nickname, String content) {
        this(nickname, content, new Date());
    }

    /**
     * Obtiene el apodo del usuario.
     *
     * @return El apodo del usuario que envía el mensaje.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Obtiene el contenido del mensaje.
     *
     * @return El contenido del mensaje.
     */
    public String getContent() {
        return content;
    }

    /**
     * Obtiene la fecha del mensaje en formato "dd/MM/yyyy HH:mm".
     *
     * @return La fecha del mensaje.
     */
    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formatter.format(date);
    }

    /**
     * Genera una representación en cadena del mensaje.
     *
     * @return La representación en cadena del mensaje.
     */
    @Override
    public String toString() {
        return  CommunicationCode.SEND_MESSAGE.getCode() + '|' +
                nickname + '|' +
                content + '|' +
                getDate();
    }
}