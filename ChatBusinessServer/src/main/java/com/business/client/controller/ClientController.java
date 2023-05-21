package com.business.client.controller;

import java.io.*;
import java.net.Socket;

/**
 * Controlador del cliente que se encarga de manejar las conexiones y comunicación con el servidor.
 */
public class ClientController implements Runnable {

    private Socket clientSocket;
    // Esto permite enviar datos desde el cliente al servidor.
    private PrintWriter out;
    // Esto permite leer los datos que llegan al cliente desde el servidor.
    private BufferedReader in;
    private String username;
    private OutputStream outputStream;

    /**
     * Crea un objeto ClientController con el socket de cliente especificado.
     *
     * @param socket socket del cliente.
     */
    public ClientController(Socket socket) {
        this.clientSocket = socket;
    }

    /**
     * Crea un objeto ClientController sin especificar el socket del cliente.
     */
    public ClientController() {
    }

    /**
     * Inicia la ejecución del hilo de control del cliente.
     */
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Método principal del hilo de control del cliente.
     * Se encarga de establecer la comunicación con el servidor y procesar las solicitudes del cliente.
     */
    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Procesa las solicitudes del cliente aquí
                System.out.println("Mensaje recibido: " + inputLine);

                // Envía una respuesta al cliente
                out.println("Respuesta del servidor: " + inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Envía un arreglo de bytes al servidor.
     *
     * @param bytes arreglo de bytes a enviar.
     */
    public void sendBytes(byte[] bytes) {
        OutputStream outputStream = null;
        try {
            outputStream = clientSocket.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Envía un subconjunto del arreglo de bytes al servidor.
     *
     * @param bytes  arreglo de bytes a enviar.
     * @param length cantidad de bytes a enviar.
     */
    public void sendBytes(byte[] bytes, int length) {
        OutputStream outputStream = null;
        try {
            outputStream = clientSocket.getOutputStream();
            outputStream.write(bytes, 0, length);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Envía un mensaje al servidor.
     *
     * @param message mensaje a enviar.
     */
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    /**
     * Obtiene el nombre de usuario del cliente.
     *
     * @return nombre de usuario del cliente.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario del cliente.
     *
     * @param username nombre de usuario del cliente.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene el socket del cliente.
     *
     * @return socket del cliente.
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * Obtiene el flujo de salida del cliente.
     *
     * @return flujo de salida del cliente.
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Establece el flujo de salida del cliente.
     *
     * @param outputStream flujo de salida del cliente.
     */
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
