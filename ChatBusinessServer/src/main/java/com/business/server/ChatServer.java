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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Clase que representa el servidor de chat.
 */
public class ChatServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private UserManager userManager;
    private MessageManager messageManager;
    private MessageFileManager messageFileManager;


    /**
     * Constructor de la clase ChatServer.
     *
     * @param port Puerto en el que se iniciará el servidor.
     */
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

    /**
     * Método para iniciar el servidor y aceptar conexiones de clientes.
     */
    public void start() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                ClientHandler client = new ClientHandler(socket);
                clients.add(client);
                iniciarTemporizador(socket);
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Método para iniciar el temporizador de inactividad para un cliente.
     *
     * @param socket Socket del cliente.
     */
    private void iniciarTemporizador(Socket socket) {
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                try {
                    ClientHandler client = new ClientHandler(socket);
                    System.out.println("Tiempo de inactividad alcanzado. Cerrando conexión: " + socket);
                    socket.close();
                    clients.remove(client);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    processMessage("7|", null);
                }
            }
        };
        // Establecer el tiempo límite de inactividad en milisegundos
        long tiempoLimite = 60000; // 1 minuto

        // Crear un temporizador y programar la tarea de cierre
        Timer timer = new Timer();
        timer.schedule(task, tiempoLimite);
    }

    /**
     * Método para enviar un mensaje de chat a todos los clientes, excepto al remitente.
     *
     * @param message Mensaje a enviar.
     * @param sender  Cliente que envió el mensaje.
     */
    private void broadcastMessage(String message, ClientHandler sender) {
        int code = Integer.parseInt(message.split("\\|")[0]);
        for (ClientHandler client : clients) {
            if(client != sender)
                client.sendMessage(message);
            else if(code == 3)
                client.sendMessage(message);
        }
    }

    /**
     * Método para enviar la lista de usuarios conectados a un cliente.
     *
     * @param client Cliente al que se enviará la lista de usuarios.
     */
    private void sendUserList(ClientHandler client) {
        String userList = userManager.getUserListMessage();
        client.sendMessage(userList);
    }

    /**
     * Método para procesar un mensaje recibido del cliente.
     *
     * @param message Mensaje recibido.
     * @param sender  Cliente que envió el mensaje.
     */
    private void processMessage(String message, ClientHandler sender) {

        // Parse the message and perform the necessary actions
        String[] parts = message.split("\\|");
        int code = Integer.parseInt(parts[0]);
        XMLFileManager xmlFileManager = new XMLFileManager("C:\\Users\\MI PC\\Documents\\IngenieriaDeSistemas\\Semestre4\\ProyectoFinalProgramacion\\ChatEmpresarialX\\ChatBusinessServer\\src\\main\\java\\com\\business\\server\\ChatEmpresarialX.xml");

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
                broadcastMessage(formattedMessage, sender);
                System.out.println(sender.getNickname() + " ha enviado un mensaje" + timestamp);
                messageManager.addMessage(sender.getNickname(),chatMessage,timestamp);
                xmlFileManager.agregarMensaje(sender.nickname, chatMessage, timestamp);
                break;

            case 6:
                // File message
                FileType type;

                String timeStamp01 = parts[2];
                String fileType = parts[3];
                String fileContent = parts[4];
                String fileMessage = "6|" + sender.getNickname() + "|" + parts[2] + "|" + fileType + "|" + fileContent;
                broadcastMessage(fileMessage, sender);

                if(fileType.equals("jpg")) type = FileType.JPG;
                else if (fileType.equals("jpeg"))  type = FileType.JPEG;
                else type = FileType.PDF;
                System.out.println(sender.getNickname() + " ha enviado un archivo" + timeStamp01);
                messageFileManager.addMessage(sender.getNickname(),fileContent,parts[2],type);
                xmlFileManager.agregarArchivo(sender.getNickname(), fileContent);
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

    /**
     * Clase interna que representa un cliente conectado al servidor.
     */
    private class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String nickname;
        private ScheduledExecutorService executor;
        private ScheduledFuture<?> temporizador;

        /**
         * Constructor de la clase ClientHandler.
         *
         * @param socket Socket del cliente.
         */
        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Método para ejecutar la lógica del cliente.
         */
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    processMessage(message, this);
                    reiniciarTemporizador(socket);
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
                    // Reiniciar el temporizador de inactividad
                    processMessage("7|", this);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Método para reiniciar el temporizador de inactividad.
         */
        private void reiniciarTemporizador(Socket socket) {
            if (executor == null) {
                executor = Executors.newSingleThreadScheduledExecutor();
            } else {
                // Cancelar la tarea anterior si existe
                if (temporizador != null) {
                    temporizador.cancel(false);
                }
            }

            // Establecer el tiempo límite de inactividad en segundos
            long tiempoLimite = 60; // 1 minuto

            temporizador = executor.schedule(() -> {
                try {
                    ClientHandler client = new ClientHandler(socket);
                    System.out.println("Tiempo de inactividad alcanzado. Cerrando conexión: " + socket);
                    socket.close();
                    clients.remove(client);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, tiempoLimite, TimeUnit.SECONDS);
        }

        /**
         * Método para enviar un mensaje al cliente.
         *
         * @param message Mensaje a enviar.
         */
        public void sendMessage(String message) {
            writer.println(message);
        }

        /**
         * Método para obtener el apodo del cliente.
         *
         * @return Apodo del cliente.
         */
        public String getNickname() {
            return nickname;
        }

        /**
         * Método para establecer el apodo del cliente.
         *
         * @param nickname Apodo del cliente.
         */
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        /**
         * Método para detener el cliente y cerrar los flujos de entrada/salida.
         */
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

    /**
     * Método principal para iniciar el servidor.
     *
     * @param args Argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        int port = 5000; // Change the port as needed
        ChatServer server = new ChatServer(port);
        server.start();
    }

}