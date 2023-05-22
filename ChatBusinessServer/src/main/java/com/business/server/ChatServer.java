package com.business.server;

import com.business.server.managers.MessageFileManager;
import com.business.server.managers.MessageManager;
import com.business.server.managers.UserManager;
import com.business.server.model.FileType;
import com.business.server.util.XMLFileManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private UserManager userManager;
    private MessageManager messageManager;
    private MessageFileManager messageFileManager;

    public ChatServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clients = new ArrayList<>();
            userManager = new UserManager();
            messageManager = new MessageManager();
            messageFileManager = new MessageFileManager();
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                ClientHandler client = new ClientHandler(socket);
                clients.add(client);
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastMessage(String message,ClientHandler sender) {
        int code = Integer.parseInt(message.split("\\|")[0]);
        for (ClientHandler client : clients) {
            if(client != sender)
                client.sendMessage(message);
            else if(code == 3)
                client.sendMessage(message);
        }
    }

    private void sendUserList(ClientHandler client) {
        String userList = userManager.getUserListMessage();
        client.sendMessage(userList);
    }

    private void processMessage(String message, ClientHandler sender) {
        // Parse the message and perform the necessary actions
        String[] parts = message.split("\\|");
        int code = Integer.parseInt(parts[0]);
        XMLFileManager xmlFileManager = new XMLFileManager("/home/afva/Documentos/projects_java/ChatEmpresarialX/ChatBusinessServer/src/main/java/com/business/server/ChatEmpresarialX.xml");

        switch (code) {
            case 1:
                // Establish connection
                String nickname = parts[1];

                System.out.println(nickname);
                if (userManager.addUser(nickname)) {
                    sender.setNickname(nickname);
                    sender.sendMessage("2|conectado");
                    broadcastMessage(userManager.getUserListMessage(),sender);
                    xmlFileManager.agregarConexion(nickname);
                } else {
                    sender.sendMessage("2|no conectado");
                    sender.stopClient();
                    sender.interrupt();
                }
                break;

            case 4:
                // Request user list
                sendUserList(sender);
                break;

            case 5:
                // Chat message
                String chatMessage = parts[2];
                String timestamp = parts[3];
                String formattedMessage = "5|" + sender.getNickname() + "|" + chatMessage + "|" + timestamp;
                broadcastMessage(formattedMessage,sender);
                messageManager.addMessage(sender.getNickname(),chatMessage,timestamp);
                xmlFileManager.agregarMensaje(sender.nickname, chatMessage, timestamp);
                break;

            case 6:
                // File message
                FileType type;

                String fileType = parts[3];
                String fileContent = parts[4];
                String fileMessage = "6|" + sender.getNickname() + "|" + parts[2] + "|" + fileType + "|" + fileContent;
                broadcastMessage(fileMessage,sender);

                if(fileType.equals("jpg")) type = FileType.JPG;
                else if (fileType.equals("jpeg"))  type = FileType.JPEG;
                else type = FileType.PDF;

                messageFileManager.addMessage(sender.getNickname(),fileContent,parts[2],type);
                xmlFileManager.agregarArchivo(sender.getNickname(), fileContent, fileMessage);
                break;

            case 7:
                // Close session
                userManager.removeUser(sender.getNickname());
                sender.sendMessage("7|" + sender.getNickname());
                broadcastMessage(userManager.getUserListMessage(),sender);
                xmlFileManager.agregarUsuarioConectado(sender.getNickname());
                sender.interrupt();
                sender.stopClient();
                clients.remove(sender);

                break;

            default:
                // Invalid message code
                break;
        }
    }


    private class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String nickname;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    processMessage(message, this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Lógica para cerrar el cliente y los recursos relacionados
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    if (writer != null) {
                        writer.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendMessage(String message) {
            writer.println(message);
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void stopClient() {
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int port = 5000; // Change the port as needed
        ChatServer server = new ChatServer(port);
        server.start();
    }

}