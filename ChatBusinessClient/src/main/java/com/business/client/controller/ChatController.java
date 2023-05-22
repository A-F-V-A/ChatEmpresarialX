package com.business.client.controller;

import com.business.client.model.Chat;
import com.business.client.model.ChatFile;
import com.business.client.model.Message;
import com.business.client.view.ChatView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class ChatController {

    /* Field to store the reference to the Stage */
    private Stage primaryStage;

    /* Business logic */
    private Chat chat;

    /* Logical interface */
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextArea I_newMessage;
    @FXML
    private VBox messageContainer;
    @FXML
    private Button B_sendMessage;
    @FXML
    private Button B_sendFIle;
    @FXML
    private Button B_connect;


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initialize() {
        B_sendFIle.setDisable(true);
        B_sendFIle.setMouseTransparent(true);
        B_sendFIle.setCursor(Cursor.WAIT);
        I_newMessage.setDisable(true);
        I_newMessage.setMouseTransparent(true);
        I_newMessage.setCursor(Cursor.WAIT);
        B_sendMessage.setDisable(true);
        B_sendMessage.setMouseTransparent(true);
        B_sendMessage.setCursor(Cursor.WAIT);
    }


    @FXML
    protected void sendMessage(ActionEvent e){

        String text = I_newMessage.getText();
        if(!text.isEmpty()){

            Message newMessage = new Message(chat.getNickname(),text);
            messageContainer.getChildren().add(ChatView.Message(newMessage));
            I_newMessage.setText("");
            // Scroll to bottom
            scrollPane.applyCss();
            scrollPane.layout();
            scrollPane.setVvalue(1.0);

            chat.getConnection().sendMessage(newMessage.toString());

        }

    }
    @FXML
    protected void sendFile(ActionEvent e) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("File PDF", "*.pdf"),
                    new FileChooser.ExtensionFilter("File JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("File JPEG", "*.jpeg")
            );

            /* Show the file selection dialog */
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        ChatFile newFile = new ChatFile(chat.getNickname(), selectedFile);

        messageContainer.getChildren().add(ChatView.MessageFile(newFile));
        scrollPane.applyCss();
        scrollPane.layout();
        scrollPane.setVvalue(1.0);





    } catch (Exception err) {
        System.err.println(err);
    }
}
    @FXML
    protected void startConnection(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/business/client/modal-connection.fxml"));
            Parent root = loader.load();
            ModalConnectionController modal = loader.getController();

            Scene sceneModal = new Scene(root);
            Stage stageModal = new Stage();
            stageModal.initModality(Modality.APPLICATION_MODAL);
            stageModal.setScene(sceneModal);
            stageModal.showAndWait();



            Chat newChat = modal.getNewChat();
            if(newChat != null){
                chat = newChat;
                if(chat.getConnection().connect(newChat.connectSession())){
                    setVisual();
                    chat.getConnection().Hear();
                }else{
                    System.err.println("Error de connection");
                }

            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    private void setVisual(){

        B_sendFIle.setDisable(false);
        B_sendFIle.setMouseTransparent(false);
        B_sendFIle.setCursor(Cursor.HAND);
        I_newMessage.setDisable(false);
        I_newMessage.setMouseTransparent(false);
        I_newMessage.setCursor(Cursor.HAND);
        B_sendMessage.setDisable(false);
        B_sendMessage.setMouseTransparent(false);
        B_sendMessage.setCursor(Cursor.HAND);
        B_connect.setDisable(true);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}



