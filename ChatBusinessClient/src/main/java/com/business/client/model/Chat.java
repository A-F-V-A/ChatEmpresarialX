package com.business.client.model;


import com.business.client.util.ChatConnection;

import java.io.*;

/**
 * Clase que representa un objeto Chat que permite la comunicación básica con un servidor de chat.
 */
public class Chat {
    private final int port;
    private final String serverAddress;
    private final String nickname;
    private final ChatConnection connection;

    /**
     * Constructor de la clase Chat.
     *
     * @param port           Puerto para la conexión del chat.
     * @param serverAddress  Dirección del servidor de chat.
     * @param nickname       Apodo o nombre de usuario para el chat.
     * @throws IOException   Excepción lanzada en caso de error de entrada/salida durante la creación del ChatConnection.
     */
    public Chat(int port, String serverAddress, String nickname) throws IOException {
        this.port = port;
        this.serverAddress = serverAddress;
        this.nickname = nickname;

        /* connection */
        this.connection = new ChatConnection(this.serverAddress, this.port);
    }

    /**
     * Obtiene el apodo o nombre de usuario asociado al Chat.
     *
     * @return El apodo o nombre de usuario del Chat.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Obtiene la conexión del Chat.
     *
     * @return La conexión del Chat.
     */
    public ChatConnection getConnection() {
        return connection;
    }


    /**
     * Establece la comunicación básica del chat para el servidor.
     *
     * @return La cadena que representa la solicitud de establecer la conexión junto con el apodo del Chat.
     */
    public String connectSession(){
        return CommunicationCode.ESTABLISH_CONNECTION.getCode() +'|'+ nickname;
    }

    /**
     * Realiza una solicitud de lista de usuarios al servidor.
     *
     * @return La cadena que representa la solicitud de lista de usuarios junto con el apodo del Chat.
     */
    public String requestUserLIst(){
        return CommunicationCode.REQUEST_USER_LIST.getCode() +'|'+ nickname;
    }

    /**
     * Realiza la desconexión del chat.
     *
     * @return La cadena que representa la solicitud de cerrar la sesión junto con el apodo del Chat.
     */
    public String signOff(){
        return CommunicationCode.CLOSE_SESSION.getCode() +'|'+ nickname;
    }

}