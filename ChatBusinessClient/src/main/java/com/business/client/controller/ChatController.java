package com.business.client.controller;

import com.business.client.model.Chat;
import com.business.client.model.ChatFile;
import com.business.client.model.Message;
import com.business.client.view.ChatView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ChatController {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextArea I_newMessage;
    @FXML
    private VBox messageContainer;

    private Stage primaryStage; // Campo para almacenar la referencia al Stage

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @FXML
    protected void sendMessage(ActionEvent e){

        String text = I_newMessage.getText();
        if(!text.isEmpty()){
            Chat newChat = new Chat(80,"192.163.0.1", "afva");
            Message newMessage = new Message(newChat.getNickname(),text);
            System.out.println(newMessage.toString());
            messageContainer.getChildren().add(ChatView.Message(newMessage));
            I_newMessage.setText("");
            // Scroll to bottom
            scrollPane.applyCss();
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        }

    }

    @FXML
    protected void sendFile(ActionEvent e) {
        Chat newChat = new Chat(80, "192.163.0.1", "afva");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("File PDF", "*.pdf"),
                    new FileChooser.ExtensionFilter("File JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("File JPEG", "*.jpeg")
            );

            /* Show the file selection dialog */
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            ChatFile newFile = new ChatFile(newChat.getNickname(), selectedFile);

            messageContainer.getChildren().add(ChatView.MessageFile(newFile));
            scrollPane.applyCss();
            scrollPane.layout();
            scrollPane.setVvalue(1.0);

        } catch (Exception err) {
            System.err.println(err);
        }
    }


}



