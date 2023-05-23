package com.business.client.controller;

import com.business.client.model.Chat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


public class ModalConnectionController {

    @FXML
    private TextField I_nickname;
    @FXML
    private TextField I_serverAddress;
    @FXML
    private TextField I_port;
    @FXML
    private Button B_connect;
    @FXML
    private Button B_cancel;
    private Chat newChat;

    /**
     * Maneja el evento de iniciar la conexión.
     *
     * @param e El evento del botón
     */
    @FXML
    protected void startConnection(ActionEvent e) {
        /*192.168.1.14*/
        String nickname = this.I_nickname.getText();
        String serverAddress = this.I_serverAddress.getText();
        int port =  Integer.parseInt(this.I_port.getText());

        try {
            newChat = new Chat(port,serverAddress, nickname);
            Stage stage = (Stage) this.B_connect.getScene().getWindow();
            stage.close();
        }catch (IOException err){
            System.err.println(err.getMessage());
        }
    }

    /**
     * Maneja el evento de cancelar la conexión.
     *
     * @param e El evento del botón
     */
    @FXML
    protected void cancelConnection(ActionEvent e){
        newChat = null;
        Stage stage = (Stage) this.B_cancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Obtiene el objeto Chat generado en la ventana modal.
     *
     * @return El objeto Chat
     */
    public Chat getNewChat() {
        return newChat;
    }

}
