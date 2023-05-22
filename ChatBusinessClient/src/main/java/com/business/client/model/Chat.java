package com.business.client.model;


import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Chat implements Runnable{

    private int port;
    private String serverAddress;
    private String nickname;
    private boolean  running;

    private Socket connection;
    private List<Message> messageList;
    private List<ChatFile> chatFileList;
    private List<String> userList;

    public Chat(int port, String serverAddress, String nickname) throws IOException {
        this.port = port;
        this.serverAddress = serverAddress;
        this.nickname = nickname;
        this.messageList = new ArrayList<>();
        this.chatFileList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.connection = new Socket(this.serverAddress, this.port);
        this.running = true;
    }

    public void addMessage(Message message) {
        messageList.add(message);
    }

    public void addFile(ChatFile chatFIle) {
        chatFileList.add(chatFIle);
    }

    public void addUser(String nickname) {
        userList.add(nickname);
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public List<ChatFile> getFileList() {
        return chatFileList;
    }

    public List<String> getUserList() {
        return userList;
    }

    public int getPort() {
        return port;
    }

    public String getNickname() {
        return nickname;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public Socket getConnection() {
        return connection;
    }

    public String connectSession(){
        return CommunicationCode.ESTABLISH_CONNECTION.getCode() +'|'+ nickname;
    }

    public String requestUserLIst(){
        return CommunicationCode.REQUEST_USER_LIST.getCode() +'|'+ nickname;
    }

    public String signOff(){
        return CommunicationCode.CLOSE_SESSION.getCode() +'|'+ nickname;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputConnection = new DataInputStream(connection.getInputStream());
            System.out.println("Inicio la escucha");
            while (running) {
                // Lee la respuesta del servidor
                String respuesta = inputConnection.readUTF();

                // Procesa la respuesta recibida, por ejemplo, imprímela en la consola
                System.out.println("Respuesta del servidor: " + respuesta);
            }
            connection.close();
            // Cierra la conexión después de detener el hilo de escucha
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}